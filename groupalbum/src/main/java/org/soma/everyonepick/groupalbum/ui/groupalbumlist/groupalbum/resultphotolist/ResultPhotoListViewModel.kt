package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.content.Context
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
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
import org.soma.everyonepick.groupalbum.domain.Checkable.Companion.setIsCheckboxVisible
import org.soma.everyonepick.groupalbum.domain.Checkable.Companion.toCheckedItemList
import org.soma.everyonepick.groupalbum.domain.model.ResultPhotoModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class ResultPhotoListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _resultPhotoModelList = MutableStateFlow<MutableList<ResultPhotoModel>>(mutableListOf())
    val resultPhotoModelList: StateFlow<MutableList<ResultPhotoModel>> = _resultPhotoModelList

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun readResultPhotoModelList(groupAlbumId: Long?) {
        viewModelScope.launch {
            try {
                _isApiLoading.value = true
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                _resultPhotoModelList.value = groupAlbumUseCase.readResultPhotoList(token, groupAlbumId!!)
                _isApiLoading.value = false
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_result_photo)
            }
        }
    }

    fun deleteCheckedResultPhotoList(groupAlbumId: Long?) {
        viewModelScope.launch {
            try {
                // TODO
                /*val token = dataStoreUseCase.bearerAccessToken.first()!!
                val checkedPhotoIdList = _resultPhotoModelList.value.toCheckedItemList()
                    .map { PhotoId(it.resultPhoto.id) }
                groupAlbumUseCase.deleteResultPhotoList(token, groupAlbumId!!, PhotoIdListRequest(checkedPhotoIdList))
                readPhotoModelList(groupAlbumId)*/
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_delete_result_photo)
            }
        }
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _resultPhotoModelList.value.setIsCheckboxVisible(isCheckboxVisible)
        _resultPhotoModelList.value = _resultPhotoModelList.value.map { it.copy() }.toMutableList()
    }
}