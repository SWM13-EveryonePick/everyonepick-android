package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

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
    val photoModelList = MutableLiveData<MutableList<PhotoModel>>()
    val isApiLoading = MutableLiveData(true)

    fun fetchPhotoModelList(groupAlbumId: Long) {
        isApiLoading.value = true

        val newPhotoModelList = photoUseCase.getPhotoModelList(groupAlbumId)
        photoModelList.value = newPhotoModelList

        isApiLoading.value = false
    }

    fun addPhotoModel(photoItem: PhotoModel) {
        photoModelList.value?.add(photoItem)
        photoModelList.value = photoModelList.value
    }

    fun deleteCheckedItems() {
        if (photoModelList.value == null) return

        val newPhotoModelList = mutableListOf<PhotoModel>()
        for(i in 0 until photoModelList.value!!.size) {
            if (!photoModelList.value!![i].isChecked)
                newPhotoModelList.add(photoModelList.value!![i])
        }
        photoModelList.value = newPhotoModelList
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        if (photoModelList.value == null) return

        for(i in 0 until photoModelList.value!!.size) {
            val newItem = copyPhotoModel(photoModelList.value!![i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            photoModelList.value!![i] = newItem
        }
        photoModelList.value = photoModelList.value
    }

    private fun copyPhotoModel(photoItem: PhotoModel) =
        PhotoModel(photoItem.photo.copy(), photoItem.isChecked, photoItem.isCheckboxVisible)
}