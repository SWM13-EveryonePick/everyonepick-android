package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _photoModelList = MutableStateFlow<MutableList<PhotoModel>>(mutableListOf())
    val photoModelList: StateFlow<MutableList<PhotoModel>> = _photoModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun readPhotoModelList(groupAlbumId: Long?) {
        viewModelScope.launch {
            try {
                _isApiLoading.value = true
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                _photoModelList.value = groupAlbumUseCase.readPhotoList(token, groupAlbumId!!)
                _isApiLoading.value = false
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_photo)
            }
        }
    }

    fun createPhotoList(groupAlbumId: Long?, images: List<MultipartBody.Part>) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                groupAlbumUseCase.createPhotoList(token, groupAlbumId!!, images)
                readPhotoModelList(groupAlbumId)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_create_photo)
            }
        }
    }

    fun deleteCheckedPhotoList(groupAlbumId: Long?) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val photoIdList = _photoModelList.value.filter { it.isChecked.value }.map { PhotoId(it.photo.id) }
                groupAlbumUseCase.deletePhotoList(token, groupAlbumId!!, PhotoIdListRequest(photoIdList))
                readPhotoModelList(groupAlbumId)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_delete_photo)
            }
        }
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _photoModelList.value = _photoModelList.value.map {
            PhotoModel(it.photo, MutableStateFlow(false), isCheckboxVisible = isCheckboxVisible)
        }.toMutableList()
    }
}