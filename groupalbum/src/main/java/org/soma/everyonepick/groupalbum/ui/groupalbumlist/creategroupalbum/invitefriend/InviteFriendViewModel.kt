package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import androidx.lifecycle.*
import com.kakao.sdk.talk.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.util.KakaoUtil.Companion.toUserWithClientId
import org.soma.everyonepick.groupalbum.domain.Checkable.Companion.toCheckedItemList
import org.soma.everyonepick.groupalbum.domain.model.InviteFriendModel
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import java.lang.Integer.max
import javax.inject.Inject

@HiltViewModel
class InviteFriendViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    /**
     * [inviteFriendModelList]는 전체 친구 리스트이자 '실제' 데이터인 반면, [filteredList]는 검색 필터링이 적용된
     * 결과이며 보여주기용 데이터입니다. 이에 따라 체크박스가 클릭되거나 하여 데이터 변경이 발생할 때는 [filteredList] 대신
     * [inviteFriendModelList]에 변경 사항이 적용되어야 합니다.
     */
    private val _inviteFriendModelList = MutableStateFlow<MutableList<InviteFriendModel>>(mutableListOf())
    val inviteFriendModelList: StateFlow<MutableList<InviteFriendModel>> = _inviteFriendModelList

    private val _filteredList = MutableStateFlow<MutableList<InviteFriendModel>>(mutableListOf())
    val filteredList: StateFlow<MutableList<InviteFriendModel>> = _filteredList

    /**
     * 기존 단체공유앨범에 속한 멤버들의 clientId 리스트입니다. 친구 목록에 있으나 여기에 속한 멤버들은
     * 리스트에 표시하지 않습니다. 이에 대한 필터링은 [filteredList]에 적용됩니다.
     */
    private val existingUserClientIdList = MutableStateFlow(listOf<String>())

    /**
     * 검색 키워드이며 [filteredList]에 필터링 적용되는 값입니다.
     */
    val keyword = MutableStateFlow("")

    /**
     * 기존 단체공유앨범에 초대하는 경우, 초대할 수 있는 인원은 기존 멤버수 만큼 줄어들기 때문에 변동됩니다.
     */
    private val _maxInviteCount = MutableStateFlow(9)
    val maxInviteCount: StateFlow<Int> = _maxInviteCount

    /**
     * 체크된 체크박스의 개수이며 [filteredList]에서 체크박스 상태가 변경될 때 같이 업데이트 되어야 합니다.
     */
    private val _checked = MutableStateFlow(0)
    val checked: StateFlow<Int> = _checked

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    init {
        savedStateHandle.get<Array<String>>(EXISTING_USER_CLIENT_ID_LIST)?.let {
            existingUserClientIdList.value = it.toList()
            _maxInviteCount.value -= it.count()
        }

        readInviteFriendModelList()

        viewModelScope.launch {
            _inviteFriendModelList.collectLatest { inviteFriendModelList ->
                updateFilteredList()

                // checked 값 바인딩
                inviteFriendModelList.forEach {
                    viewModelScope.launch {
                        it.isChecked.collectLatest { isChecked ->
                            if (isChecked) _checked.value += 1
                            // 초기에 체크박스가 체크 해제된 채로 있기 때문에 아래 코드가 수행되게 됩니다. 이 때문에
                            // checked 값이 0이 아니라 음수로 초기화되는 문제가 있었고, 이를 해결하고자 하는 의도입니다.
                            else _checked.value = max(0, _checked.value - 1)
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            keyword.collectLatest {
                updateFilteredList()
            }
        }
    }

    fun getCheckedFriendList() = _inviteFriendModelList.value.toCheckedItemList()
        .map { it.friend }
        .toMutableList()

    private fun readInviteFriendModelList() {
        _isApiLoading.value = true
        friendUseCase.readFriends({ _isApiLoading.value = false }) { newFriends ->
            _inviteFriendModelList.value = newFriends.elements.getInviteFriendModelList()
        }
    }

    private fun List<Friend>?.getInviteFriendModelList() = this?.map {
        InviteFriendModel(it, MutableStateFlow(false))
    }?.toMutableList() ?: mutableListOf()

    private fun updateFilteredList() {
        _filteredList.value = inviteFriendModelList.value
            .filter { it.friend.profileNickname?.contains(keyword.value) ?: false }
            // 추가로 existingUserClientId에 있지 않은 경우만 보여주는 필터링을 적용합니다.
            .filter { inviteFriendModel ->
                !existingUserClientIdList.value.any { existingUserClientId ->
                    existingUserClientId == inviteFriendModel.friend.toUserWithClientId().clientId.toString()
                }
            }
            .toMutableList()
    }


    companion object {
        private const val EXISTING_USER_CLIENT_ID_LIST = "existingUserClientIdList"
    }
}