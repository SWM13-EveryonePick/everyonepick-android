package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import javax.inject.Inject

@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
): ViewModel() {
    val groupAlbumItemList = MutableLiveData<MutableList<GroupAlbumItem>>()
    init {
        updateGroupAlbumList()
    }

    fun updateGroupAlbumList() {
        val newGroupAlbumItemList = groupAlbumRepository.getGroupAlbumItemList()
        groupAlbumItemList.value = newGroupAlbumItemList
    }

    fun addGroupAlbum(groupAlbumItem: GroupAlbumItem) {
        groupAlbumItemList.value?.add(groupAlbumItem)
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun updateTitle(position: Int, title: String) {
        if(groupAlbumItemList.value == null) return

        val newItem = GroupAlbumItem(
            groupAlbumItemList.value!![position].groupAlbum.copy(),
            groupAlbumItemList.value!![position].isChecked,
            groupAlbumItemList.value!![position].isCheckboxVisible
        )
        newItem.groupAlbum.title = title

        groupAlbumItemList.value!![position] = newItem
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun setCheckboxGone() {
        if(groupAlbumItemList.value == null) return

        for(i in 0 until groupAlbumItemList.value!!.size) {
            val newItem = GroupAlbumItem(
                groupAlbumItemList.value!![i].groupAlbum.copy(),
                false,
                false
            )
            groupAlbumItemList.value!![i] = newItem
        }
        groupAlbumItemList.value = groupAlbumItemList.value
    }

    fun setCheckboxVisible() {
        if(groupAlbumItemList.value == null) return

        for(i in 0 until groupAlbumItemList.value!!.size) {
            val newItem = GroupAlbumItem(
                groupAlbumItemList.value!![i].groupAlbum.copy(),
                false,
                true
            )
            groupAlbumItemList.value!![i] = newItem
        }
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
}