package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.PhotoUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoUseCase: PhotoUseCase
): ViewModel() {
    private val _photoModelList = MutableStateFlow<MutableList<PhotoModel>>(mutableListOf())
    val photoModelList: StateFlow<MutableList<PhotoModel>> = _photoModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    fun readPhotoModelList(groupAlbumId: Long) {
        viewModelScope.launch {
            _isApiLoading.value = true
            _photoModelList.value = photoUseCase.readPhotoModelList(groupAlbumId)
            _isApiLoading.value = false
        }
    }

    fun deleteCheckedItems() {
        _photoModelList.value = _photoModelList.value.filter { !it.isChecked }.toMutableList()
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _photoModelList.value = _photoModelList.value.map {
            PhotoModel(it.photo, isChecked = false, isCheckboxVisible = isCheckboxVisible)
        }.toMutableList()
    }
}