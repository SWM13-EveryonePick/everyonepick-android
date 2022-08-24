package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetail
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadList
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

/**
 * [bearerAccessToken] -> [me] & [groupAlbum] -> [memberModelList] 순서의 의존성이 있음에 유의합니다.
 */
@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataStoreUseCase: DataStoreUseCase,
    private val userUseCase: UserUseCase,
    groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    private val bearerAccessToken = dataStoreUseCase.bearerAccessToken
    val me: StateFlow<User> = bearerAccessToken.transformLatest {
        if (it != null) emit(userUseCase.readUser(it))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), User.dummyData)

    private val _groupAlbum = MutableStateFlow(GroupAlbumReadDetail(-1, "", -1, listOf(), listOf()))
    val groupAlbum: StateFlow<GroupAlbumReadDetail> = _groupAlbum

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
                _memberModelList.value = it.toMemberModelList()
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

    fun getCheckedUserList() = _memberModelList.value.getActualData()
        .filter { it.isChecked }
        .map { it.user }
        .toMutableList()

    fun setPhotoSelectionMode(selectionMode: SelectionMode) {
        _photoSelectionMode.value = selectionMode.ordinal
    }

    fun setMemberSelectionMode(selectionMode: SelectionMode) {
        _memberSelectionMode.value = selectionMode.ordinal
    }

    fun onClickCheckbox(position: Int, isChecked: Boolean) {
        _memberModelList.value.data[position].isChecked = isChecked
        if (isChecked) _checked.value += 1
        else _checked.value -= 1
    }

    fun setGroupAlbum(groupAlbumReadDetail: GroupAlbumReadDetail) {
        _groupAlbum.value = groupAlbumReadDetail
    }
}