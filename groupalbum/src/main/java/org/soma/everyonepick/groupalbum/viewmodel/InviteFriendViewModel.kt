package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.data.item.InviteFriendItem
import org.soma.everyonepick.groupalbum.data.repository.FriendRepository
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class InviteFriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {
    val keyword = MutableStateFlow("")
    val inviteFriendItemList: MutableLiveData<MutableList<InviteFriendItem>> = MutableLiveData()
    val checked = MutableLiveData(0)
    val isApiLoading = MutableLiveData(true)

    init {
        viewModelScope.launch {
            keyword.collectLatest {
                fetchInviteFriendItemList(it)
            }
        }
    }

    private fun fetchInviteFriendItemList(keyword: String) {
        isApiLoading.value = true
        friendRepository.fetchFriends({ isApiLoading.value = false }) { newFriends ->
            val newInviteFriendItemList = convertFriendsToInviteFriendItemList(newFriends)
                .filter { it.friend.profileNickname?.contains(keyword) ?: false }.toMutableList()
            inviteFriendItemList.postValue(newInviteFriendItemList)
        }
    }

    private fun convertFriendsToInviteFriendItemList(friends: Friends<Friend>): MutableList<InviteFriendItem> {
        val result = mutableListOf<InviteFriendItem>()
        friends.elements?.let { friendList ->
            friendList.forEach {
                result.add(InviteFriendItem(it, isChecked = false))
            }
        }
        return result
    }

    fun getCheckedFriendList(): MutableList<Friend> {
        val checkedFriendList = mutableListOf<Friend>()
        inviteFriendItemList.value?.let {
            for(inviteFriendItem in it) {
                if(inviteFriendItem.isChecked) checkedFriendList.add(inviteFriendItem.friend)
            }
        }
        return checkedFriendList
    }
}