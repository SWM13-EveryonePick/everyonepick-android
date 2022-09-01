package org.soma.everyonepick.camera.ui.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
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
import java.io.File
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

    private val _latestImage = MutableStateFlow<Bitmap?>(null)
    val latestImage: StateFlow<Bitmap?> = _latestImage


    init {
        viewModelScope.launch {
            _selectedPosePackIndex.collect {
                readPoseModelList()
            }
        }

        viewModelScope.launch {
            _posePackModelList.collect {
                readPoseModelList()
            }
        }
    }


    fun readPosePackModelList() {
        viewModelScope.launch {
            val token = dataStoreUseCase.bearerAccessToken.first()!!
            _posePackModelList.value = posePackUseCase.readPosePackList(token)
        }
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

    /**
     * 가장 최근 사진을 불러온 뒤 [latestImage]에 저장합니다.
     */
    fun readLatestImage(context: Context) {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )
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
}