package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _photoModelList = MutableStateFlow<MutableList<PhotoModel>>(mutableListOf())
    val photoModelList: StateFlow<MutableList<PhotoModel>> = _photoModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    fun readPhotoModelList(groupAlbumId: Long) {
        viewModelScope.launch {
            _isApiLoading.value = true
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                _photoModelList.value = groupAlbumUseCase.readPhotoList(token, groupAlbumId)
            } catch (e: Exception) { }
            _isApiLoading.value = false
        }
    }

    fun deleteCheckedItems() {
        _photoModelList.value = _photoModelList.value.filter { !it.isChecked.value }.toMutableList()
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _photoModelList.value = _photoModelList.value.map {
            PhotoModel(it.photo, MutableStateFlow(false), isCheckboxVisible = isCheckboxVisible)
        }.toMutableList()
    }
}