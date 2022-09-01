package org.soma.everyonepick.camera.ui.preview

import android.R
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.*
import androidx.camera.core.Camera
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.databinding.FragmentPreviewBinding
import org.soma.everyonepick.common.util.CameraUtil.Companion.rotate
import org.soma.everyonepick.common.util.CameraUtil.Companion.toBitmap
import org.soma.everyonepick.common.util.FileUtil.Companion.saveBitmapInPictureDirectory
import org.soma.everyonepick.common.util.HomeActivityUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
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
    private val orientationEventListener by lazy {
        object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation : Int) {
                imageCapture?.targetRotation = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
            }
        }
    }

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
        viewModel.readLatestImage(requireContext())

        return binding.root
    }


    private fun subscribeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
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

                launch {
                    viewModel.lensFacing.collectLatest {
                        cameraExecutor = Executors.newSingleThreadExecutor()
                        binding.previewview.post { setUpCamera() }
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

    /** [CameraX] 관련 함수 */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            processCameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = processCameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val screenAspectRatio = AspectRatio.RATIO_4_3

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
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(viewModel.lensFacing.value)
                .build()

            preview?.setSurfaceProvider(binding.previewview.surfaceProvider)
            camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }


    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
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

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        cameraExecutor.shutdown()
    }


    /** [PreviewFragmentListener] */
    override fun onClickGalleryButton() {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onClickShutterButton() {
        imageCapture?.let { imageCapture ->
            val callback = object: ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeOptInUsageError")
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    image.image?.toBitmap()?.rotate(image.imageInfo.rotationDegrees)?.let {
                        saveBitmapInPictureDirectory(it, requireContext(), lifecycleScope)
                        viewModel.setLatestImage(it) // 최근 사진을 업데이트합니다.
                    }
                    image.close()
                }
            }
            imageCapture.takePicture(cameraExecutor, callback)
        }
    }

    override fun onClickPosePackButton() {
        viewModel.switchIsPosePackShown()
    }

    override fun onClickPreview() {
        if (viewModel.isPosePackShown.value) viewModel.switchIsPosePackShown()
    }

    override fun onClickSwitchLensFacing() {
        viewModel.switchLensFacing()
    }

    companion object {
        private const val TAG = "PreviewFragment"
        private const val ANIMATION_DURATION = 300L
    }
}

interface PreviewFragmentListener {
    fun onClickGalleryButton()
    fun onClickShutterButton()
    fun onClickPosePackButton()
    fun onClickPreview()
    fun onClickSwitchLensFacing()
}