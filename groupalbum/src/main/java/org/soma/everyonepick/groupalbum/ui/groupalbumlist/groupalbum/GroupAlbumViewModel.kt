package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetailDto
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadListDto
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    // Fragment가 args를 통해 group album id를 가지고 있으므로, Fragment단에서 초기화를 진행합니다.
    val groupAlbum = MutableLiveData(GroupAlbumReadDetailDto(-1, "Loading", -1, listOf(), listOf()))
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)
    var me: User? = null

    // Drawer
    val memberSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)
    var memberModelList = MutableLiveData(MemberModelList())
    val checked = MutableLiveData(0)

    // 하위 Fragment
    val photoSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)
    // TODO: 합성중 / 합성완료 모드

    fun updateGroupAlbumTitle(newTitle: String) {
        val newGroupAlbum = groupAlbum.value?.copy(title = newTitle)
        groupAlbum.value = newGroupAlbum
    }

    suspend fun fetchMemberList() {
        memberModelList.value?.data = userUseCase.getMemberList(groupAlbum.value?.id?: -1)
        memberModelList.value = memberModelList.value
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        memberModelList.value?.setIsCheckboxVisible(isCheckboxVisible)
        memberModelList.value = memberModelList.value
    }

    fun removeCheckedItems() {
        memberModelList.value?.removeCheckedItems()
        memberModelList.value = memberModelList.value
    }
}