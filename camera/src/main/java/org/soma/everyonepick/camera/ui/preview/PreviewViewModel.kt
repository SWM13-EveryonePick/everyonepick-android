package org.soma.everyonepick.camera.ui.preview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.data.entity.PosePack
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.camera.domain.usecase.PosePackUseCase
import org.soma.everyonepick.camera.domain.usecase.PoseUseCase
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val posePackUseCase: PosePackUseCase,
    private val poseUseCase: PoseUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _posePackModelList = MutableStateFlow(mutableListOf<PosePackModel>())
    val posePackModelList: StateFlow<MutableList<PosePackModel>> = _posePackModelList

    private val _selectedPosePackIndex = MutableStateFlow(0)
    val selectedPosePackIndex: StateFlow<Int> = _selectedPosePackIndex

    private val _poseModelList = MutableStateFlow(mutableListOf<PoseModel>())
    val poseModelList: StateFlow<MutableList<PoseModel>> = _poseModelList

    private val _selectedPoseIndex = MutableStateFlow<Int?>(null)
    val selectedPoseIndex: StateFlow<Int?> = _selectedPoseIndex

    private val _isPosePackShown = MutableStateFlow(false)
    val isPosePackShown: StateFlow<Boolean> = _isPosePackShown

    init {
        viewModelScope.launch {
            _selectedPosePackIndex.collect {
                onSelectPosePackModel(it)
            }
        }
    }

    fun readPosePackModelList() {
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            _posePackModelList.value = posePackUseCase.readPosePackList(token)

            onSelectPosePackModel(_selectedPosePackIndex.value)
        }
    }

    private fun onSelectPosePackModel(selectedIndex: Int) {
        _posePackModelList.value = _posePackModelList.value.mapIndexed { index, item ->
            PosePackModel(item.id, item.name, index == selectedIndex)
        }.toMutableList()

        readPoseModelList()
    }

    private fun readPoseModelList() {
        val posePackId = _posePackModelList.value.elementAtOrNull(_selectedPosePackIndex.value)?.id ?: return
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            _poseModelList.value = poseUseCase.readPoseList(token, posePackId)
        }
    }

    fun switchIsPosePackShown() {
        _isPosePackShown.value = !_isPosePackShown.value
    }

    fun setSelectedPosePackIndex(position: Int) {
        _selectedPosePackIndex.value = position
    }

    fun setSelectedPoseIndex(index: Int?) {
        _selectedPoseIndex.value = index
    }
}