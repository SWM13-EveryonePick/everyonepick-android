package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.PhotoDao
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

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        if(photoItemList.value == null) return

        for(i in 0 until photoItemList.value!!.size) {
            val newItem = copyPhotoItem(photoItemList.value!![i])
            newItem.isCheckboxVisible = isCheckboxVisible
            photoItemList.value!![i] = newItem
        }
        photoItemList.value = photoItemList.value
    }

    private fun copyPhotoItem(photoItem: PhotoItem) =
        PhotoItem(photoItem.photoDao.copy(), photoItem.isChecked, photoItem.isCheckboxVisible)
}