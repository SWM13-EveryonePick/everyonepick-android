package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.timeout

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.dto.PickRequest
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class TimeoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val dataStoreUseCase: DataStoreUseCase,
    private val groupAlbumUseCase: GroupAlbumUseCase
): ViewModel() {
    private val _hour = MutableStateFlow(0)
    private val _min1 = MutableStateFlow(0)
    val min1: StateFlow<Int> = _min1
    private val _min2 = MutableStateFlow(0)

    /**
     * EditText가 전부 채워졌을 때만 확인 버튼을 enable로 두는데, 이를 체크하기 위한 값으로 3일 때 버튼을 enable합니다.
     */
    private val _filledEditText = MutableStateFlow(0)
    val filledEditText: StateFlow<Int> = _filledEditText

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun setHour(hour: Int) {
        _hour.value = hour
    }

    fun setMin1(min: Int) {
        _min1.value = min
    }

    fun setMin2(min: Int) {
        _min2.value = min
    }

    fun plusFilledEditText() {
        _filledEditText.value += 1
    }

    fun minusFilledEditText() {
        _filledEditText.value -= 1
    }

    fun createPick(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val groupAlbumId = savedStateHandle.get<Long>("groupAlbumId")?: -1
                val pickId = groupAlbumUseCase.createPick(token, groupAlbumId, createPickRequest()).id

                onSuccess.invoke(pickId)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_create_pick)
            }
        }
    }

    private fun createPickRequest(): PickRequest {
        val selectedPhotoIdList = savedStateHandle.get<LongArray>("photoIdList")?: longArrayOf()
        return PickRequest(
            calculateTimeoutAsMin(),
            selectedPhotoIdList.map { PhotoId(it) }
        )
    }

    private fun calculateTimeoutAsMin() = (_hour.value*60 + _min1.value*10 + _min2.value).toLong()

    fun createPickInfo(pickId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val selectedPhotos = savedStateHandle.get<LongArray>("selectedPhotoIdList")?.map { PhotoId(it) }?: listOf()
                groupAlbumUseCase.createPickInfo(
                    token,
                    savedStateHandle["groupAlbumId"]?: -1,
                    pickId,
                    PhotoIdListRequest(selectedPhotos)
                )

                onSuccess.invoke()
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_create_pick_info)
            }
        }
    }
}