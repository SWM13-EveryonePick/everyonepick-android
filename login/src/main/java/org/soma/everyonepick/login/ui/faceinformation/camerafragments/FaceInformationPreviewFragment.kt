package org.soma.everyonepick.login.ui.faceinformation.camerafragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import org.soma.everyonepick.login.databinding.FaceInformationCameraUiContainerBinding
import org.soma.everyonepick.login.databinding.FragmentFaceInformationPreviewBinding

import org.soma.everyonepick.login.util.FROnnxMobileNet
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class FaceInformationPreviewFragment : Fragment() {
    private var _binding: FragmentFaceInformationPreviewBinding? = null
    private val binding get() = _binding!!

    private var cameraUiContainerBinding: FaceInformationCameraUiContainerBinding? = null

    private var processCameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.previewview.post {
            updateCameraUi()
            setUpCamera()
        }
    }

    private fun updateCameraUi() {
        cameraUiContainerBinding?.root?.let {
            binding.layoutRoot.removeView(it)
        }

        cameraUiContainerBinding = FaceInformationCameraUiContainerBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.layoutRoot,
            true
        )
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            processCameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        val screenAspectRatio = calculateAspectRatio()?: return
        val cameraProvider = processCameraProvider?: return
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        // Preview
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()

        // ImageAnalyzer
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, FROnnxMobileNet(requireContext()) { floatArray ->
                    Log.d(TAG, floatArray.contentToString())
                })
            }

        cameraProvider.unbindAll()

        try {
            preview?.setSurfaceProvider(binding.previewview.surfaceProvider)
            camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner, cameraSelector, preview, imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    private fun calculateAspectRatio(): Int? {
        val parentActivity = activity?: return null

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = parentActivity.windowManager.currentWindowMetrics.bounds
            aspectRatio(metrics.width(), metrics.height())
        } else {
            val metrics = resources.displayMetrics
            aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        return if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        cameraExecutor.shutdown()
    }


    companion object {
        private const val TAG = "PreviewFragment"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}