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
    val isApiLoading = MutableLiveData(true) // TODO: true -> Retrofit2 -> false

    fun fetchPhotoItemList(groupAlbumId: Long) {
        val newPhotoItemList = photoRepository.getPhotoItemList(groupAlbumId)
        photoItemList.value = newPhotoItemList

        isApiLoading.value = false
    }

    fun addPhotoItem(photoItem: PhotoItem) {
        photoItemList.value?.add(photoItem)
        photoItemList.value = photoItemList.value
    }

    fun deleteCheckedItems() {
        if(photoItemList.value == null) return

        val newPhotoItemList = mutableListOf<PhotoItem>()
        for(i in 0 until photoItemList.value!!.size) {
            if(!photoItemList.value!![i].isChecked)
                newPhotoItemList.add(photoItemList.value!![i])
        }
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