package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.item.InviteFriendItem
import org.soma.everyonepick.groupalbum.data.repository.FriendRepository
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class InviteFriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {
    val inviteFriendItemList: MutableLiveData<MutableList<InviteFriendItem>> = MutableLiveData()
    val isApiLoading = MutableLiveData(true)
    init {
        fetchInviteFriendItemList()
    }

    fun fetchInviteFriendItemList() {
        isApiLoading.value = true
        friendRepository.fetchFriends({ isApiLoading.value = false }) { newFriends ->
            val newInviteFriendItemList = convertFriendsToInviteFriendItemList(newFriends)
            inviteFriendItemList.postValue(newInviteFriendItemList)
        }
    }

    private fun convertFriendsToInviteFriendItemList(friends: Friends<Friend>): MutableList<InviteFriendItem> {
        val result = mutableListOf<InviteFriendItem>()
        friends.elements?.let { friendList ->
            for(friend in friendList) {
                result.add(InviteFriendItem(friend, isChecked = false))
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