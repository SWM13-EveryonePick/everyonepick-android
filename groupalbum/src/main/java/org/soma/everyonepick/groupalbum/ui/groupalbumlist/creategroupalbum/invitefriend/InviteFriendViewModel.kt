package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.util.KakaoUtil.Companion.toUserWithClientId
import org.soma.everyonepick.common.util.KakaoUtil.Companion.toUserWithClientIdWithoutKakaoPrefix
import org.soma.everyonepick.groupalbum.domain.model.InviteFriendModel
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import javax.inject.Inject

@HiltViewModel
class InviteFriendViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {
    val keyword = MutableStateFlow("")

    /**
     * 기존 단체공유앨범에 속한 멤버들의 clientId(without "kakao_") 리스트입니다. 친구 목록에 있으나 여기에 속한 멤버들은
     * 리스트에 표시하지 않습니다. 이에 대한 필터링은 filteredList에 적용됩니다.
     */
    val existingUserClientIdList = MutableStateFlow(listOf<String>())

    /**
     * inviteFriendModelList는 전체 친구 리스트이자 '실제' 데이터인 반면, filteredList는 검색 필터링이 적용된 결과이며
     * 보여주기용 '임시' 데이터입니다. 이에 따라 체크박스가 클릭되거나 하여 데이터 변경이 발생할 때 filteredList 대신
     * inviteFriendModelList에 대해서 적용해야 합니다.
     */
    val inviteFriendModelList: MutableLiveData<MutableList<InviteFriendModel>> = MutableLiveData()
    val filteredList: MutableLiveData<MutableList<InviteFriendModel>> = MutableLiveData()

    var maxInviteCount = MutableLiveData(9)
    val checked = MutableLiveData(0)
    val isApiLoading = MutableLiveData(true)

    init {
        readInviteFriendModelList()
        viewModelScope.launch {
            keyword.collectLatest {
                updateFilteredListByKeyword(it)
            }
        }
    }

    private fun readInviteFriendModelList() {
        isApiLoading.value = true
        friendUseCase.readFriends({ isApiLoading.value = false }) { newFriends ->
            val newInviteFriendModelList = newFriends.toInviteFriendModelList()
            inviteFriendModelList.value = newInviteFriendModelList

            // inviteFriendModelList의 값이 변경되었으나 실제로 보여주는 데이터는 filteredList이므로
            // filteredList의 뷰 갱신을 유도합니다.
            updateFilteredListByKeyword("")
        }
    }

    private fun updateFilteredListByKeyword(keyword: String) {
        if (inviteFriendModelList.value.isNullOrEmpty()) return

        val newFilteredList = inviteFriendModelList.value!!
            .filter { it.friend.profileNickname?.contains(keyword) ?: false }
            // 추가로 existingUserClientId에 있지 않은 경우만 보여주는 필터링을 적용합니다.
            .filter { inviteFriendModel ->
                !existingUserClientIdList.value.any { existingUserClientId ->
                    existingUserClientId == inviteFriendModel.friend.id.toString()
                }
            }.toMutableList()
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
        inviteFriendModelList.value?.let {
            for(inviteFriendItem in it) {
                if (inviteFriendItem.isChecked) checkedFriendList.add(inviteFriendItem.friend)
            }
        }
        return checkedFriendList
    }
}