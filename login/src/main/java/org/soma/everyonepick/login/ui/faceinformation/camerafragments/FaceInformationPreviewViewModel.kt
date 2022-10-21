package org.soma.everyonepick.login.ui.faceinformation.camerafragments

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.common_ui.util.ImageUtil.Companion.toBitmap
import org.soma.everyonepick.common_ui.util.ImageUtil.Companion.toByteArray
import org.soma.everyonepick.login.R
import javax.inject.Inject

@HiltViewModel
class FaceInformationPreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreUseCase: DataStoreUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    @SuppressLint("UnsafeOptInUsageError")
    fun uploadFaceInfo(imageProxy: ImageProxy, onSuccess: () -> Unit, doOnEnd: () -> Unit) {
        viewModelScope.launch {
            var responseMessage = ""
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val requestBody = imageProxy.image?.toByteArray()?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", "face_${userUseCase.readUser(token).id}", requestBody!!)
                responseMessage = userUseCase.uploadFaceInfo(token, part).message

                onSuccess.invoke()
            } catch (e: Exception) {
                Log.e("uploadFaceInfo", responseMessage)
                Log.e("uploadFaceInfo", e.toString())
                _toastMessage.value = "${context.getString(R.string.toast_failed_to_upload_face_info)} $responseMessage"
            }
            doOnEnd.invoke()
        }
    }
}