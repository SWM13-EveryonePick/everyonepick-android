package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import javax.inject.Inject

@HiltViewModel
class GroupAlbumViewModel @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
): ViewModel() {
    val groupAlbumList: MutableLiveData<List<GroupAlbum>> = MutableLiveData()
    init {
        updateGroupAlbumList()
    }

    fun updateGroupAlbumList() {
        val newGroupAlbumList = groupAlbumRepository.getGroupAlbumList()
        groupAlbumList.postValue(newGroupAlbumList)
    }
}