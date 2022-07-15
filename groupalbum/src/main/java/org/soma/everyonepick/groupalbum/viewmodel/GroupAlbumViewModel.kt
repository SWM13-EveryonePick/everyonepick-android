package org.soma.everyonepick.groupalbum.viewmodel

import android.util.Log
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
    val groupAlbumItemList: MutableLiveData<MutableList<GroupAlbumItem>> = MutableLiveData()
    init {
        updateGroupAlbumList()
    }

    fun updateGroupAlbumList() {
        val newGroupAlbumItemList = groupAlbumRepository.getGroupAlbumItemList()
        groupAlbumItemList.postValue(newGroupAlbumItemList)
    }

    fun addGroupAlbum(groupAlbumItem: GroupAlbumItem) {
        groupAlbumItemList.value?.let{
            it.add(groupAlbumItem)
            groupAlbumItemList.postValue(it)
        }
    }
}