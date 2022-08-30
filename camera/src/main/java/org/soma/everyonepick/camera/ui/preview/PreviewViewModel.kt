package org.soma.everyonepick.camera.ui.preview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.camera.domain.usecase.PosePackUseCase
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val posePackUseCase: PosePackUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _posePackModelList = MutableStateFlow(mutableListOf<PosePackModel>())
    val posePackModelList: StateFlow<MutableList<PosePackModel>> = _posePackModelList

    private val _isPosePackShown = MutableStateFlow(false)
    val isPosePackShown: StateFlow<Boolean> = _isPosePackShown

    private val _selectedPosePackIndex = MutableStateFlow(0)
    val selectedPosePackIndex: StateFlow<Int> = _selectedPosePackIndex

    init {
        viewModelScope.launch {
            _selectedPosePackIndex.collect {
                updateIsSelectedOfPosePackModel(it)
            }
        }
    }

    fun readPosePackModelList() {
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            _posePackModelList.value = posePackUseCase.readPosePackList(token)

            // PosePackModelList 선택 처리
            updateIsSelectedOfPosePackModel(_selectedPosePackIndex.value)
        }
    }

    private fun updateIsSelectedOfPosePackModel(selectedIndex: Int) {
        _posePackModelList.value = _posePackModelList.value.mapIndexed { index, item ->
            PosePackModel(item.id, item.name, index == selectedIndex)
        }.toMutableList()
    }

    fun switchIsPosePackShown() {
        _isPosePackShown.value = !_isPosePackShown.value
    }

    fun setSelectedPosePackIndex(position: Int) {
        _selectedPosePackIndex.value = position
    }
}