package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.Checkable.Companion.setIsCheckboxVisible
import org.soma.everyonepick.common.domain.Checkable.Companion.toCheckedItemList
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.dto.ResultPhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.entity.PhotoId
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

    fun readResultPhotoModelList(groupAlbumId: Long?, doOnEnd: () -> Unit = {}) {
        if (groupAlbumId == null || groupAlbumId == GroupAlbum.dummyData.id) return

        viewModelScope.launch {
            try {
                _isApiLoading.value = true
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                _resultPhotoModelList.value = groupAlbumUseCase.readResultPhotoList(token, groupAlbumId!!)
                _isApiLoading.value = false
            } catch (e: Exception) {
                _toastMessage.value = ""
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_result_photo)
            }
        }

        doOnEnd.invoke()
    }

    fun deleteCheckedResultPhotoList(groupAlbumId: Long?) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val checkedPhotoIdList = _resultPhotoModelList.value.toCheckedItemList()
                    .map { PhotoId(it.resultPhoto.id) }
                groupAlbumUseCase.deleteResultPhotoList(token, groupAlbumId!!, ResultPhotoIdListRequest(checkedPhotoIdList))
                _resultPhotoModelList.value = groupAlbumUseCase.readResultPhotoList(token, groupAlbumId)
            } catch (e: Exception) {
                _toastMessage.value = ""
                _toastMessage.value = context.getString(R.string.toast_failed_to_delete_result_photo)
            }
        }
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        _resultPhotoModelList.value.setIsCheckboxVisible(isCheckboxVisible)
        _resultPhotoModelList.value = resultPhotoModelList.value.map { it.copy() }.toMutableList()
    }
}