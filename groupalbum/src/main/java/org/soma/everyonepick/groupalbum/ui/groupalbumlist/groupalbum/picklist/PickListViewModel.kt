package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.picklist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.PickDetail
import org.soma.everyonepick.groupalbum.domain.model.PickModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PickListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    /**
     * [_pickModelList]에서 각각 아이템을 [PickModel.isDone]을 기준으로 나누어 각각 [_completedPickModelList],
     * [_uncompletedPickModelList]로 흘려보냅니다.
     */
    private val _pickModelList = MutableStateFlow<MutableList<PickModel>>(mutableListOf())

    private val _completedPickModelList = MutableStateFlow<MutableList<PickModel>>(mutableListOf())
    val completedPickModelList: StateFlow<MutableList<PickModel>> = _completedPickModelList

    private val _uncompletedPickModelList = MutableStateFlow<MutableList<PickModel>>(mutableListOf())
    val uncompletedPickModelList: StateFlow<MutableList<PickModel>> = _uncompletedPickModelList

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    init {
        viewModelScope.launch {
            _pickModelList.collectLatest { pickModelList ->
                _completedPickModelList.value = pickModelList.filter { it.isDone }.toMutableList()
                _uncompletedPickModelList.value = pickModelList.filter { !it.isDone }.toMutableList()
            }
        }
    }

    fun readPickModelList(groupAlbumId: Long) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                _pickModelList.value = groupAlbumUseCase.readPickList(token, groupAlbumId)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_pick)
            }
        }
    }

    fun readPickDetail(groupAlbumId: Long, pickId: Long, onSuccess: (PickDetail) -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val pickDetail = groupAlbumUseCase.readPickDetail(token, groupAlbumId, pickId)
                onSuccess.invoke(pickDetail)
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_pick)
            }
        }
    }
}