package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.PhotoUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoUseCase: PhotoUseCase
): ViewModel() {
    val photoItemList = MutableLiveData<MutableList<PhotoModel>>()
    val isApiLoading = MutableLiveData(true)

    fun fetchPhotoModelList(groupAlbumId: Long) {
        isApiLoading.value = true

        val newPhotoModelList = photoUseCase.getPhotoModelList(groupAlbumId)
        photoItemList.value = newPhotoModelList

        isApiLoading.value = false
    }

    fun addPhotoModel(photoItem: PhotoModel) {
        photoItemList.value?.add(photoItem)
        photoItemList.value = photoItemList.value
    }

    fun deleteCheckedItems() {
        if (photoItemList.value == null) return

        val newPhotoModelList = mutableListOf<PhotoModel>()
        for(i in 0 until photoItemList.value!!.size) {
            if (!photoItemList.value!![i].isChecked)
                newPhotoModelList.add(photoItemList.value!![i])
        }
        photoItemList.value = newPhotoModelList
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        if (photoItemList.value == null) return

        for(i in 0 until photoItemList.value!!.size) {
            val newItem = copyPhotoModel(photoItemList.value!![i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            photoItemList.value!![i] = newItem
        }
        photoItemList.value = photoItemList.value
    }

    private fun copyPhotoModel(photoItem: PhotoModel) =
        PhotoModel(photoItem.photo.copy(), photoItem.isChecked, photoItem.isCheckboxVisible)
}