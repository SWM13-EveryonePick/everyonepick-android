package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import java.lang.Integer.max
import javax.inject.Inject

@HiltViewModel
class PickViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    val maxPickCount = 5

    private val _photoModelList = MutableStateFlow<List<PhotoModel>>(listOf())
    val photoModelList: StateFlow<List<PhotoModel>> = _photoModelList

    private val _checked = MutableStateFlow(0)
    val checked: StateFlow<Int> = _checked

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

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


    fun createPickInfo(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                groupAlbumUseCase.createPickInfo(
                    token,
                    savedStateHandle[GROUP_ALBUM_ID]?: -1L,
                    savedStateHandle[PICK_ID]?: -1L,
                    PhotoIdListRequest(getSelectedPhotoIdList().map { PhotoId(it) })
                )
                onSuccess.invoke()
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_create_pick_info)
            }
        }
    }

    companion object {
        private const val GROUP_ALBUM_ID = "groupAlbumId"
        private const val PICK_ID = "pickId"
    }
}