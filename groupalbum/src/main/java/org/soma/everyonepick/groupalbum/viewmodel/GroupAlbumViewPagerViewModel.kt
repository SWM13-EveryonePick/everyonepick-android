package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.foundation.data.model.User
import org.soma.everyonepick.common.data.repository.UserRepository
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.data.itemlist.MemberModelList
import org.soma.everyonepick.groupalbum.data.model.GroupAlbum
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@HiltViewModel
class GroupAlbumViewPagerViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    // Fragment가 args를 통해 group album id를 가지고 있으므로, Fragment단에서 초기화를 진행합니다.
    val groupAlbum = MutableLiveData(GroupAlbum(-1, "Loading", -1, listOf(), -1))
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)
    var me: User? = null

    // Drawer
    val memberSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)
    var memberItemList = MutableLiveData(MemberModelList())
    val checked = MutableLiveData(0)

    // 하위 Fragment
    val photoSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)
    // TODO: 합성중 / 합성완료 모드

    fun updateGroupAlbumTitle(newTitle: String) {
        val newGroupAlbum = groupAlbum.value?.copy(title = newTitle)
        groupAlbum.value = newGroupAlbum
    }

    suspend fun fetchMemberList() {
        memberItemList.value?.data = userUseCase.getMemberList(groupAlbum.value?.id?: -1)
        memberItemList.value = memberItemList.value
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        memberItemList.value?.setIsCheckboxVisible(isCheckboxVisible)
        memberItemList.value = memberItemList.value
    }

    fun removeCheckedItems() {
        memberItemList.value?.removeCheckedItems()
        memberItemList.value = memberItemList.value
    }
}