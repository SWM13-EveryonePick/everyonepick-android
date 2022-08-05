package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import javax.inject.Inject

/**
 * groupAlbumItemList의 값을 수정하더라도 MutableLiveData의 특성으로 인해
 * 주소값이 변하여야 observer가 작동하므로, 데이터를 변경할 때 이를 감안하여야 합니다.
 *
 * GroupAlbum RecyclerView의 설계상, 가장 마지막 아이템을 '생성 버튼' 취급하게 되므로
 * 마지막 아이템에는 [GroupAlbumItem.dummyData]가 위치하는 것을 보장해야 합니다.
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
): ViewModel() {
    val groupAlbumItemList = MutableLiveData<MutableList<GroupAlbumItem>>()
    val isApiLoading = MutableLiveData(true)

    init {
        fetchGroupAlbumItemList()
    }

    /**
     * [GroupAlbumRepository]에서 데이터를 가져온 뒤 값을 적용합니다.
     */
    fun fetchGroupAlbumItemList() {
        isApiLoading.value = true

        val newGroupAlbumItemList = groupAlbumRepository.getGroupAlbumItemList()
        newGroupAlbumItemList.add(GroupAlbumItem.dummyData()) // 더미데이터 추가
        groupAlbumItemList.value = newGroupAlbumItemList

        isApiLoading.value = false
    }

    fun addGroupAlbumItem(groupAlbumItem: GroupAlbumItem) {
        groupAlbumItemList.value?.let {
            // 가장 마지막 아이템(생성 버튼) 이전 위치에 삽입해야 합니다.
            it.add(it.size-1, groupAlbumItem)
            groupAlbumItemList.value = it
        }
    }

    fun deleteGroupAlbum(id: Long) {
        if (groupAlbumItemList.value == null) return

        val newGroupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumItemList.value!!.size) {
            if (groupAlbumItemList.value!![i].groupAlbumDao.id != id)
                newGroupAlbumItemList.add(groupAlbumItemList.value!![i])
        }
        groupAlbumItemList.value = newGroupAlbumItemList
    }

    fun deleteCheckedItems() {
        if (groupAlbumItemList.value == null) return

        val newGroupAlbumItemList = mutableListOf<GroupAlbumItem>()
        // 가장 마지막 아이템은 생성 버튼이므로 제거해선 안 됩니다.
        for(i in 0 until groupAlbumItemList.value!!.size - 1) {
            if (!groupAlbumItemList.value!![i].isChecked)
                newGroupAlbumItemList.add(groupAlbumItemList.value!![i])
        }
        newGroupAlbumItemList.add(groupAlbumItemList.value!!.last())
        groupAlbumItemList.value = newGroupAlbumItemList
    }


    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        if (groupAlbumItemList.value == null) return

        for(i in 0 until groupAlbumItemList.value!!.size) {
            val newItem = copyGroupAlbumItem(groupAlbumItemList.value!![i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            groupAlbumItemList.value!![i] = newItem
        }
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    private fun copyGroupAlbumItem(groupAlbumItem: GroupAlbumItem) =
        GroupAlbumItem(groupAlbumItem.groupAlbumDao.copy(), groupAlbumItem.isChecked, groupAlbumItem.isCheckboxVisible)

    fun checkAll() {
        if (groupAlbumItemList.value == null) return

        val isAllGroupAlbumChecked = groupAlbumItemList.value!!.all{ it.isChecked }
        for(i in 0 until groupAlbumItemList.value!!.size) {
            val newItem = copyGroupAlbumItem(groupAlbumItemList.value!![i])
            newItem.isChecked = !isAllGroupAlbumChecked
            groupAlbumItemList.value!![i] = newItem
        }
        groupAlbumItemList.value = groupAlbumItemList.value
    }
}