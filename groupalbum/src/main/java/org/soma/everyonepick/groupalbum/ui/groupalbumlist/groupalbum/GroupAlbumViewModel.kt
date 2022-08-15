package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetail
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadList
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    // Fragment가 args를 통해 group album id를 가지고 있으므로, Fragment단에서 초기화를 진행합니다.
    val groupAlbum = MutableLiveData(GroupAlbumReadDetail(-1, "Loading", -1, listOf(), listOf()))
    val currentItem: MutableLiveData<Int> = MutableLiveData(0)
    var me: User? = null

    // Drawer
    val memberSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)

    /**
     * [GroupAlbumReadDetail.users]를 [MemberModel]로 변환한 리스트를 [MemberModelList]를 통해 홀드합니다.
     * groupAlbum를 observe하고, 값이 변경되면 [updateMemberModelList]를 통해 적절한 값을 업데이트 시켜야 합니다.
     */
    var memberModelList = MutableLiveData(MemberModelList())
    val checked = MutableLiveData(0)

    // 하위 Fragment
    val photoSelectionMode = MutableLiveData(SelectionMode.NORMAL_MODE.ordinal)
    // TODO: 합성중 / 합성완료 모드

    fun updateMemberModelList() {
        val newMemberModelList = mutableListOf<MemberModel>()
        groupAlbum.value?.users?.forEach {
            it?.let {
                newMemberModelList.add(MemberModel(it, isChecked = false, isCheckboxVisible = false))
            }
        }
        memberModelList.value = MemberModelList(newMemberModelList)
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        memberModelList.value?.setIsCheckboxVisible(isCheckboxVisible)
        memberModelList.value = memberModelList.value
    }

    fun getCheckedUserList(): MutableList<User> {
        val checkedUserList = mutableListOf<User>()
        memberModelList.value?.let {
            for (i in 0 until it.getActualItemCount()) {
                if (it.data[i].isChecked) checkedUserList.add(it.data[i].user)
            }
        }
        return checkedUserList
    }
}