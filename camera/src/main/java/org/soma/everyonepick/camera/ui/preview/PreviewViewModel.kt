package org.soma.everyonepick.camera.ui.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.data.entity.Pose
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

    private val _selectedPosePackIndex = MutableStateFlow(0)
    val selectedPosePackIndex: StateFlow<Int> = _selectedPosePackIndex


    private val _poseModelList = MutableStateFlow(mutableListOf<Pose>())
    val poseModelList: StateFlow<MutableList<Pose>> = _poseModelList

    private val _selectedPoseIndex = MutableStateFlow<Int?>(null)
    val selectedPoseIndex: StateFlow<Int?> = _selectedPoseIndex


    private val _isPosePackShown = MutableStateFlow(false)
    val isPosePackShown: StateFlow<Boolean> = _isPosePackShown

    private val _latestImage = MutableStateFlow<Bitmap?>(null)
    val latestImage: StateFlow<Bitmap?> = _latestImage

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val lensFacing: StateFlow<Int> = _lensFacing

    private val _isTakingPicture = MutableStateFlow(false)
    val isTakingPicture: StateFlow<Boolean> = _isTakingPicture


    init {
        viewModelScope.launch {
            _selectedPosePackIndex.collect {
                readPoseModelList()
            }
        }
    }

    private fun readPoseModelList() {
        val peopleNum = _posePackModelList.value.elementAtOrNull(_selectedPosePackIndex.value)?.peopleNum ?: return
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            _poseModelList.value = poseUseCase.readPoseList(token, peopleNum.toString())
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

    fun setIsTakingPicture(flag: Boolean) {
        _isTakingPicture.value = flag
    }
}