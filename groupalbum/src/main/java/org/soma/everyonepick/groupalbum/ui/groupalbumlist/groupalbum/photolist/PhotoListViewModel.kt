package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import java.io.File
import javax.inject.Inject
import kotlin.Throws

@HiltViewModel
class PhotoListViewModel @Inject constructor(
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
                _toastMessage.value = "사진을 불러오는 데 실패했습니다."
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
                _toastMessage.value = "사진을 업로드하는 데 실패했습니다."
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
                _toastMessage.value = "사진을 삭제하는 데 실패했습니다."
            }
        }
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _photoModelList.value = _photoModelList.value.map {
            PhotoModel(it.photo, MutableStateFlow(false), isCheckboxVisible = isCheckboxVisible)
        }.toMutableList()
    }
}