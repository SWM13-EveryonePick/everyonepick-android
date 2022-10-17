package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.dto.ResultPhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    private val groupAlbumId = savedStateHandle[GROUP_ALBUM_ID] ?: -1L
    private val photoId = savedStateHandle[PHOTO_ID] ?: -1L
    private val resultPhotoId = savedStateHandle[RESULT_PHOTO_ID] ?: -1L

    private val _photoUrl = MutableStateFlow(savedStateHandle[PHOTO_URL] ?: "")
    val photoUrl: StateFlow<String?> = _photoUrl

    fun deletePhotoOrResultPhoto(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                if (photoId != -1L) {
                    groupAlbumUseCase.deletePhotoList(token, groupAlbumId, PhotoIdListRequest(listOf(PhotoId(photoId))))
                } else if (resultPhotoId != -1L) {
                    groupAlbumUseCase.deleteResultPhotoList(token, groupAlbumId, ResultPhotoIdListRequest(listOf(PhotoId(resultPhotoId))))
                }
                onSuccess.invoke()
            } catch (e: Exception) {
                onFailure.invoke()
            }
        }
    }

    companion object {
        private const val GROUP_ALBUM_ID = "groupAlbumId"
        private const val PHOTO_ID = "photoId"
        private const val RESULT_PHOTO_ID = "resultPhotoId"
        private const val PHOTO_URL = "photoUrl"
    }
}