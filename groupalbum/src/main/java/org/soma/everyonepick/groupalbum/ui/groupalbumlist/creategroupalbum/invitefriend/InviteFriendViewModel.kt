package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.domain.model.InviteFriendModel
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import javax.inject.Inject

@HiltViewModel
class InviteFriendViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {
    val keyword = MutableStateFlow("")

    /**
     * inviteFriendItemList는 전체 친구 리스트이자 '실제' 데이터인 반면, filteredList는 검색 필터링이 적용된 결과이며
     * 보여주기용 '임시' 데이터입니다. 이에 따라 체크박스가 클릭되거나 하여 데이터 변경이 발생할 때 filteredList 대신
     * inviteFriendItemList에 대해서 적용해야 합니다.
     */
    val inviteFriendItemList: MutableLiveData<MutableList<InviteFriendModel>> = MutableLiveData()
    val filteredList: MutableLiveData<MutableList<InviteFriendModel>> = MutableLiveData()

    val checked = MutableLiveData(0)
    val isApiLoading = MutableLiveData(true)

    init {
        fetchInviteFriendModelList()
        viewModelScope.launch {
            keyword.collectLatest {
                updateFilteredListByKeyword(it)
            }
        }
    }

    private fun fetchInviteFriendModelList() {
        isApiLoading.value = true
        friendUseCase.readFriends({ isApiLoading.value = false }) { newFriends ->
            val newInviteFriendModelList = newFriends.toInviteFriendModelList()
            inviteFriendItemList.value = newInviteFriendModelList

            // inviteFriendItemList의 값이 변경되었으나 실제로 보여주는 데이터는 filteredList이므로
            // filteredList의 뷰 갱신을 유도합니다.
            updateFilteredListByKeyword("")
        }
    }

    private fun updateFilteredListByKeyword(keyword: String) {
        if (inviteFriendItemList.value.isNullOrEmpty()) return

        val newFilteredList = inviteFriendItemList.value!!
            .filter { it.friend.profileNickname?.contains(keyword) ?: false }.toMutableList()
        filteredList.postValue(newFilteredList)
    }

    private fun Friends<Friend>.toInviteFriendModelList(): MutableList<InviteFriendModel> {
        val result = mutableListOf<InviteFriendModel>()
        elements?.forEach {
            result.add(InviteFriendModel(it, isChecked = false))
        }
        return result
    }

    fun getCheckedFriendList(): MutableList<Friend> {
        val checkedFriendList = mutableListOf<Friend>()
        inviteFriendItemList.value?.let {
            for(inviteFriendItem in it) {
                if (inviteFriendItem.isChecked) checkedFriendList.add(inviteFriendItem.friend)
            }
        }
        return checkedFriendList
    }
}