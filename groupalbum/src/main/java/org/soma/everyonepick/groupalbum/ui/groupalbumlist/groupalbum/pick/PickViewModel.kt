package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import java.lang.Integer.max

class PickViewModel: ViewModel() {
    val maxPickCount = 5

    private val _photoModelList = MutableStateFlow<List<PhotoModel>>(listOf())
    val photoModelList: StateFlow<List<PhotoModel>> = _photoModelList

    private val _checked = MutableStateFlow(0)
    val checked: StateFlow<Int> = _checked

    init {
        viewModelScope.launch {
            _photoModelList.collectLatest { photoModelList ->
                photoModelList.forEach {
                    viewModelScope.launch {
                        it.isChecked.collectLatest { isChecked ->
                            if (isChecked) _checked.value += 1
                            else _checked.value = max(0, _checked.value - 1)
                        }
                    }
                }
            }
        }
    }

    fun setPhotoModelListByPhotoList(photoIdList: List<Long>, photoUrlList: List<String>) {
        _photoModelList.value = createPhotoList(photoIdList, photoUrlList).map {
            PhotoModel(it, isChecked = MutableStateFlow(false), isCheckboxVisible = false)
        }
    }

    private fun createPhotoList(
        photoIdList: List<Long>,
        photoUrlList: List<String>
    ) = photoIdList.mapIndexed { index, l -> Photo(l, photoUrlList[index]) }

    fun getSelectedPhotoIdList() = _photoModelList.value
        .filter { it.isChecked.value }
        .map { it.photo.id }
}