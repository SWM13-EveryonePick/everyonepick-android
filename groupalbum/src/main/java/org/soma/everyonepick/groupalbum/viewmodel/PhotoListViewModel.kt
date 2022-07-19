package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.PhotoItem
import org.soma.everyonepick.groupalbum.data.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
): ViewModel() {
    val photoItemList = MutableLiveData<MutableList<PhotoItem>>()

    fun updatePhotoItemList(groupAlbumId: Long) {
        val newPhotoItemList = photoRepository.getPhotoItemList(groupAlbumId)
        photoItemList.value = newPhotoItemList
    }
}