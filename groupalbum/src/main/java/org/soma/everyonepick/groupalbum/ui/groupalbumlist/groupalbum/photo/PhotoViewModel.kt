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
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    private val groupAlbumId: Long = savedStateHandle.get<Long>("groupAlbumId")?: -1
    private val photoId: Long = savedStateHandle.get<Long>("photoId")?: -1

    private val _photoUrl = MutableStateFlow(savedStateHandle.get<String>("photoUrl")?: "")
    val photoUrl: StateFlow<String?> = _photoUrl

    fun deletePhoto(onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                groupAlbumUseCase.deletePhotoList(token, groupAlbumId, PhotoIdListRequest(listOf(PhotoId(photoId))))
                onSuccess.invoke()
            } catch (e: Exception) {
                onFailure.invoke()
            }
        }
    }
}