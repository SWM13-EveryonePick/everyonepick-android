package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbumListItem
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import javax.inject.Inject

/**
 * groupAlbumListItems의 값을 수정하더라도 MutableLiveData의 특성으로 인해
 * 주소값이 변하여야 observer가 작동하므로, 데이터를 변경할 때 이를 감안하여야 합니다.
 */

@HiltViewModel
class GroupAlbumListViewModel @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
): ViewModel() {
    val groupAlbumListItemList = MutableLiveData<MutableList<GroupAlbumListItem>>()
    init {
        updateGroupAlbumList()
    }

    fun updateGroupAlbumList() {
        val newGroupAlbumListItems = groupAlbumRepository.getGroupAlbumListItems()
        groupAlbumListItemList.value = newGroupAlbumListItems
    }

    fun addGroupAlbum(groupAlbumListItem: GroupAlbumListItem) {
        groupAlbumListItemList.value?.add(groupAlbumListItem)
        groupAlbumListItemList.value = groupAlbumListItemList.value
    }

    fun updateTitle(position: Int, title: String) {
        if(groupAlbumListItemList.value == null) return

        val newItem = GroupAlbumListItem(
            groupAlbumListItemList.value!![position].groupAlbum.copy(),
            groupAlbumListItemList.value!![position].isChecked,
            groupAlbumListItemList.value!![position].isCheckboxVisible
        )
        newItem.groupAlbum.title = title

        groupAlbumListItemList.value!![position] = newItem
        groupAlbumListItemList.value = groupAlbumListItemList.value
    }

    fun setCheckboxGone() {
        if(groupAlbumListItemList.value == null) return

        for(i in 0 until groupAlbumListItemList.value!!.size) {
            val newItem = GroupAlbumListItem(
                groupAlbumListItemList.value!![i].groupAlbum.copy(),
                false,
                false
            )
            groupAlbumListItemList.value!![i] = newItem
        }
        groupAlbumListItemList.value = groupAlbumListItemList.value
    }

    fun setCheckboxVisible() {
        if(groupAlbumListItemList.value == null) return

        for(i in 0 until groupAlbumListItemList.value!!.size) {
            val newItem = GroupAlbumListItem(
                groupAlbumListItemList.value!![i].groupAlbum.copy(),
                false,
                true
            )
            groupAlbumListItemList.value!![i] = newItem
        }
        groupAlbumListItemList.value = groupAlbumListItemList.value
    }

    fun deleteCheckedItems() {
        if(groupAlbumListItemList.value == null) return

        val newGroupAlbumListItemList = mutableListOf<GroupAlbumListItem>()
        for(i in 0 until groupAlbumListItemList.value!!.size) {
            if(!groupAlbumListItemList.value!![i].isChecked)
                newGroupAlbumListItemList.add(groupAlbumListItemList.value!![i])
        }
        groupAlbumListItemList.value = newGroupAlbumListItemList
    }
}