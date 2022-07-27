package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.item.InviteFriendItem
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import javax.inject.Inject

class InviteFriendViewModel: ViewModel() {
    val inviteFriendItemList: MutableLiveData<MutableList<InviteFriendItem>> = MutableLiveData()
    val isApiLoading = MutableLiveData(true)
    init {
        fetchInviteFriendItemList()
    }

    fun fetchInviteFriendItemList() {
        isApiLoading.value = true

        TalkApiClient.instance.friends { newFriends, error ->
            if(error != null) {
                Log.e("TAG", "카카오톡 친구 목록 가져오기 실패", error)
            } else if(newFriends != null) {
                Log.i("TAG", "카카오톡 친구 목록 가져오기 성공")
                val newInviteFriendItemList = convertFriendsToInviteFriendItemList(newFriends)
                inviteFriendItemList.postValue(newInviteFriendItemList)
            }

            isApiLoading.value = false
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
}