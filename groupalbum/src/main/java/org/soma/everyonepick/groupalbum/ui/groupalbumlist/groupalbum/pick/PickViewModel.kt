package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel

class PickViewModel: ViewModel() {
    private var _photoModelList = MutableStateFlow<List<PhotoModel>>(listOf())
    val photoModelList: StateFlow<List<PhotoModel>> = _photoModelList

    fun setPhotoModelListByPhotoList(photoIdList: List<Long>, photoUrlList: List<String>) {
        _photoModelList.value = createPhotoList(photoIdList, photoUrlList).map {
            PhotoModel(it, isChecked = MutableStateFlow(false), isCheckboxVisible = false)
        }
    }

    private fun createPhotoList(
        photoIdList: List<Long>,
        photoUrlList: List<String>
    ) = photoIdList.mapIndexed { index, l -> Photo(l, photoUrlList[index]) }
}