package org.soma.everyonepick.camera.ui.preview

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.databinding.FragmentPreviewBinding
import org.soma.everyonepick.camera.domain.usecase.PosePackUseCase
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.util.setVisibility
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class PreviewFragment : Fragment(), PreviewFragmentListener {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by viewModels()

    private var valueAnimator: ValueAnimator? = null

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var processCameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.posePackAdapter = PosePackAdapter(viewModel)
            it.poseAdapter = PoseAdapter(viewModel)
            it.listener = this
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPosePackShown.collectLatest {
                    if (it) {
                        (activity as HomeActivityUtil).hideCameraNavigation()
                        showPosePackLayout()

                        if (viewModel.posePackModelList.value.isEmpty()) {
                            viewModel.readPosePackModelList()
                        }
                    } else {
                        (activity as HomeActivityUtil).showCameraNavigation()
                        hidePosePackLayout()
                    }
                }
            }
        }
    }

    private fun showPosePackLayout() {
        binding.layoutPosepack.doOnLayout {
            val params = binding.layoutPosepack.layoutParams as ConstraintLayout.LayoutParams
            animateBottomMargin(binding.layoutPosepack, params.bottomMargin, 0)
        }
    }

    private fun hidePosePackLayout() {
        binding.layoutPosepack.doOnLayout {
            val params = binding.layoutPosepack.layoutParams as ConstraintLayout.LayoutParams
            val height = binding.layoutPosepack.height
            animateBottomMargin(binding.layoutPosepack, params.bottomMargin, -height)
        }
    }

    private fun animateBottomMargin(view: View, start: Int, end: Int) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        if (params.bottomMargin == end) return

        valueAnimator?.cancel()

        valueAnimator = ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                params.bottomMargin = valueAnimator.animatedValue as Int
                view.layoutParams = params
            }
            duration = ANIMATION_DURATION

            start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.previewview.post {
            setUpCamera()
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

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isPosePackShown.value) viewModel.switchIsPosePackShown()
                else (activity as HomeActivityUtil).navigateToGroupAlbum()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        cameraExecutor.shutdown()
    }


    /** [PreviewFragmentListener] */
    override fun onClickGalleryButton() {
        // TODO
    }

    override fun onClickShutterButton() {
        imageCapture?.let { imageCapture ->
            // TODO: imageCapture.takePicture
        }
    }

    override fun onClickPosePackButton() {
        viewModel.switchIsPosePackShown()
    }

    override fun onClickPreview() {
        if (viewModel.isPosePackShown.value) viewModel.switchIsPosePackShown()
    }

    companion object {
        private const val TAG = "PreviewFragment"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private const val ANIMATION_DURATION = 300L
    }
}

interface PreviewFragmentListener {
    fun onClickGalleryButton()
    fun onClickShutterButton()
    fun onClickPosePackButton()
    fun onClickPreview()
}