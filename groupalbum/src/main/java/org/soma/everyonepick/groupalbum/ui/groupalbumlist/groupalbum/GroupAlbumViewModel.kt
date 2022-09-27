package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.talk.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.domain.Checkable.Companion.toCheckedItemList
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.util.SelectionMode
import java.lang.Integer.max
import javax.inject.Inject

/**
 * [bearerAccessToken] -> [me] & [groupAlbum] -> [memberModelList] 순서의 의존성이 있음을 유의해야 합니다.
 */
@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val dataStoreUseCase: DataStoreUseCase,
    private val userUseCase: UserUseCase,
    private val groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    private val bearerAccessToken = dataStoreUseCase.bearerAccessToken

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    val me: StateFlow<User> = bearerAccessToken.transformLatest {
        if (it != null) emit(userUseCase.readUser(it))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), User.dummyData)

    private val _groupAlbum = MutableStateFlow(GroupAlbum(-1, "", -1, listOf(), 0))
    val groupAlbum: StateFlow<GroupAlbum> = _groupAlbum

    private val _memberModelList = MutableStateFlow(MemberModelList())
    val memberModelList: StateFlow<MemberModelList> = _memberModelList


    private val _checked = MutableStateFlow(0)
    val checked: StateFlow<Int> = _checked

    private val _memberSelectionMode = MutableStateFlow(SelectionMode.NORMAL_MODE.ordinal)
    val memberSelectionMode: StateFlow<Int> = _memberSelectionMode

    // For ViewPager2's child fragments
    private val _viewPagerPosition = MutableStateFlow(0)
    val viewPagerPosition: StateFlow<Int> = _viewPagerPosition

    private val _photoSelectionMode = MutableStateFlow(SelectionMode.NORMAL_MODE.ordinal)
    val photoSelectionMode: StateFlow<Int> = _photoSelectionMode

    init {
        viewModelScope.launch {
            bearerAccessToken.collectLatest {
                it?.let {
                    // GroupAlbumFragment의 navArgs 값을 불러옵니다.
                    val groupAlbumId = savedStateHandle.get<Long>("groupAlbumId")?: -1
                    _groupAlbum.value = groupAlbumUseCase.readGroupAlbum(it, groupAlbumId)
                }
            }
        }

        viewModelScope.launch {
            groupAlbum.collectLatest {
                _memberModelList.value = it.getMemberModelList()
            }
        }

        viewModelScope.launch {
            _memberModelList.collectLatest { memberModelList ->
                // checked 값 바인딩
                memberModelList.data.forEach {
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
    }

    fun setViewPagerPosition(position: Int) {
        _viewPagerPosition.value = position
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _memberModelList.value.setIsCheckboxVisible(isCheckboxVisible)
        _memberModelList.value = _memberModelList.value.getNewInstance()
    }

    fun setPhotoSelectionMode(selectionMode: SelectionMode) {
        _photoSelectionMode.value = selectionMode.ordinal
    }

    fun setMemberSelectionMode(selectionMode: SelectionMode) {
        _memberSelectionMode.value = selectionMode.ordinal
    }

    fun setGroupAlbum(groupAlbum: GroupAlbum) {
        _groupAlbum.value = groupAlbum
    }

    fun inviteUsersToGroupAlbum(friendList: MutableList<Friend>) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val data = groupAlbumUseCase
                    .inviteUsersToGroupAlbum(token, groupAlbum.value.id!!, friendList)
                _groupAlbum.value = data
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_invite)
            }
        }
    }

    fun updateGroupAlbum(title: String) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val groupAlbum = groupAlbum.value.copy(title = title)
                val data = groupAlbumUseCase.updateGroupAlbum(token, groupAlbum.id!!, groupAlbum)
                _groupAlbum.value = data
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_rename_group_album)
            }
        }
    }

    fun leaveGroupAlbum() {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                groupAlbumUseCase.leaveGroupAlbum(token, groupAlbum.value.id!!)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_exit_group_album)
            }
        }
    }

    fun kickUsersOutOfGroupAlbum() {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val userListToKick = _memberModelList.value.getListWithoutDummy().toCheckedItemList()
                    .map { it.user }.toMutableList()
                val data = groupAlbumUseCase.kickUsersOutOfGroupAlbum(token, groupAlbum.value.id!!, userListToKick)
                _groupAlbum.value = data
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_kick)
            }
        }
    }
}