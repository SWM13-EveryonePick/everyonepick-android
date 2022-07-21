package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import javax.inject.Inject

/**
 * groupAlbumItemList의 값을 수정하더라도 MutableLiveData의 특성으로 인해
 * 주소값이 변하여야 observer가 작동하므로, 데이터를 변경할 때 이를 감안하여야 합니다.
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

    fun fetchGroupAlbumItemList() {
        isApiLoading.value = true

        val newGroupAlbumItemList = groupAlbumRepository.getGroupAlbumItemList()
        groupAlbumItemList.value = newGroupAlbumItemList

        isApiLoading.value = false
    }

    fun addGroupAlbumItem(groupAlbumItem: GroupAlbumItem) {
        groupAlbumItemList.value?.add(groupAlbumItem)
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun deleteCheckedItems() {
        if(groupAlbumItemList.value == null) return

        val newGroupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumItemList.value!!.size) {
            if(!groupAlbumItemList.value!![i].isChecked)
                newGroupAlbumItemList.add(groupAlbumItemList.value!![i])
        }
        groupAlbumItemList.value = newGroupAlbumItemList
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        if(groupAlbumItemList.value == null) return

        for(i in 0 until groupAlbumItemList.value!!.size) {
            val newItem = copyGroupAlbumItem(groupAlbumItemList.value!![i])
            newItem.isCheckboxVisible = isCheckboxVisible
            groupAlbumItemList.value!![i] = newItem
        }
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    private fun copyGroupAlbumItem(groupAlbumItem: GroupAlbumItem) =
        GroupAlbumItem(groupAlbumItem.groupAlbumDao.copy(), groupAlbumItem.isChecked, groupAlbumItem.isCheckboxVisible)

    fun deleteGroupAlbum(id: Long) {
        if(groupAlbumItemList.value == null) return

        val newGroupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumItemList.value!!.size) {
            if(groupAlbumItemList.value!![i].groupAlbumDao.id != id)
                newGroupAlbumItemList.add(groupAlbumItemList.value!![i])
        }
        groupAlbumItemList.value = newGroupAlbumItemList
    }
}