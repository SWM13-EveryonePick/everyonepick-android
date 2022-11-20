package org.soma.everyonepick.camera.ui.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.camera.domain.model.PoseModel
import org.soma.everyonepick.camera.domain.model.PosePackModel
import org.soma.everyonepick.camera.domain.usecase.PoseUseCase
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val poseUseCase: PoseUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _posePackModelList = MutableStateFlow((3..7).map { PosePackModel(it) }.toMutableList())
    val posePackModelList: StateFlow<MutableList<PosePackModel>> = _posePackModelList

    private val _poseModelList = MutableStateFlow(mutableListOf<PoseModel>())
    val poseModelList: StateFlow<MutableList<PoseModel>> = _poseModelList


    /** 현재 열려있는 PosePack의 Index */
    private val _currentPosePackIndex = MutableStateFlow(0)
    val currentPosePackIndex: StateFlow<Int> = _currentPosePackIndex

    private var _selectedPosePackIndex = 0
    val selectedPosePackIndex = _selectedPosePackIndex

    private val _selectedPoseId = MutableStateFlow<Long?>(null)
    val selectedPoseId: StateFlow<Long?> = _selectedPoseId

    private var selectedPoseIndex: Int? = null


    private val _isPosePackShown = MutableStateFlow(false)
    val isPosePackShown: StateFlow<Boolean> = _isPosePackShown

    private val _latestImage = MutableStateFlow<Bitmap?>(null)
    val latestImage: StateFlow<Bitmap?> = _latestImage

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val lensFacing: StateFlow<Int> = _lensFacing


    init {
        viewModelScope.launch {
            currentPosePackIndex.collect {
                readPoseModelList()
            }
        }
    }

    private fun readPoseModelList() {
        val peopleNum = _posePackModelList.value.elementAtOrNull(currentPosePackIndex.value)?.peopleNum ?: return
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            val newPoseModelList = poseUseCase.readPoseList(token, peopleNum.toString())
            // 이전에 열어두었던 pose가 있을 경우 isChecked를 true로 둡니다.
            newPoseModelList.forEach {
                if (it.pose.id == selectedPoseId.value) it.isChecked.value = true
            }
            _poseModelList.value = newPoseModelList
        }
    }

    fun switchIsPosePackShown() {
        _isPosePackShown.value = !_isPosePackShown.value
    }

    fun setCurrentPosePackIndex(position: Int) {
        _currentPosePackIndex.value = position
    }

    fun onClickPoseItem(index: Int?) {
        // 이전에 선택했던 pose 선택 해제
        if (selectedPoseIndex != null && selectedPoseIndex != index) {
            _poseModelList.value[selectedPoseIndex!!].isChecked.value = false
            val prev = _poseModelList.value
            _poseModelList.value = mutableListOf()
            _poseModelList.value = prev.toMutableList()
        }

        selectedPoseIndex = index
        _selectedPosePackIndex = _currentPosePackIndex.value
        _selectedPoseId.value = index?.let { poseModelList.value[it].pose.id }
    }

    fun getSelectedPoseModel(): PoseModel? {
        return selectedPoseIndex?.let { poseModelList.value[it] }
    }

    /**
     * 가장 최근 사진을 불러온 뒤 [latestImage]에 저장합니다.
     */
    fun readLatestImage(context: Context) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null
        val sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)
        if (cursor != null && cursor.moveToFirst()) {
            val imageLocation = cursor.getString(0)
            if (File(imageLocation).exists()) {
                val bitmap = BitmapFactory.decodeFile(imageLocation)
                setLatestImage(bitmap)
            }
        }
    }

    fun setLatestImage(bitmap: Bitmap) {
        _latestImage.value = bitmap
    }

    fun switchLensFacing() {
        _lensFacing.value =
            if (_lensFacing.value == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
    }
}