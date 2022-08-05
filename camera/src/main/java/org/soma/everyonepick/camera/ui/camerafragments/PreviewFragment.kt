package org.soma.everyonepick.camera.ui.camerafragments

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
import org.soma.everyonepick.camera.databinding.CameraUiContainer2Binding
import org.soma.everyonepick.camera.databinding.FragmentPreviewBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private var cameraUiContainerBinding: CameraUiContainer2Binding? = null

    private var processCameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
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

        cameraUiContainerBinding = CameraUiContainer2Binding.inflate(
            LayoutInflater.from(requireContext()),
            binding.layoutRoot,
            true
        )

        cameraUiContainerBinding?.imagebuttonShutter?.setOnClickListener {
            imageCapture?.let { imageCapture ->
                // TODO: imageCapture.takePicture
            }
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            processCameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        val screenAspectRatio = calculateAspectRatio()
        val cameraProvider = processCameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        // TODO: 화면 전환
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // Preview
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setJpegQuality(50)
            .build()

        cameraProvider.unbindAll()

        try {
            preview?.setSurfaceProvider(binding.previewview.surfaceProvider)
            camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    private fun calculateAspectRatio(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics.bounds
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