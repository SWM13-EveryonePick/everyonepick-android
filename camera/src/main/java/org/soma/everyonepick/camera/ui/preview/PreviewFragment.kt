package org.soma.everyonepick.camera.ui.preview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.camera.databinding.FragmentPreviewBinding
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.binding.bindImageView
import org.soma.everyonepick.common_ui.util.FileUtil.Companion.saveBitmapInPictureDirectory
import org.soma.everyonepick.common_ui.util.ImageUtil.Companion.rotate
import org.soma.everyonepick.common_ui.util.ImageUtil.Companion.toBitmap
import java.util.concurrent.Executors


@AndroidEntryPoint
class PreviewFragment : Fragment(), PreviewFragmentListener {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by viewModels()

    private var bottomMarginValueAnimator: ValueAnimator? = null
    private var shutterObjectAnimator: ObjectAnimator? = null

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var processCameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraExecutor = Executors.newSingleThreadExecutor()

    private lateinit var poseImageScaleGestureDetector: ScaleGestureDetector

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
                    viewModel.selectedPoseId.collectLatest {
                        if (it != null) {
                            viewModel.getSelectedPoseModel()?.let { poseModel ->
                                bindImageView(binding.imagePose, poseModel.pose.poseUrl)
                            }
                        } else {
                            binding.imagePose.setImageResource(0)
                        }
                    }
                }

                launch {
                    viewModel.isPosePackShown.collectLatest {
                        if (it) {
                            (activity as HomeActivityUtil).hideCameraNavigation()
                            showPosePackLayout()
                        } else {
                            (activity as HomeActivityUtil).showCameraNavigation()
                            hidePosePackLayout()
                        }
                    }
                }

                launch {
                    viewModel.lensFacing.collectLatest {
                        binding.previewview.post { setUpCamera() }
                    }
                }
            }
        }

        setPreviewViewOnTouchListenerForPoseImage()
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

        bottomMarginValueAnimator?.cancel()
        bottomMarginValueAnimator = ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                params.bottomMargin = valueAnimator.animatedValue as Int
                view.layoutParams = params
            }
            duration = ANIMATION_DURATION
            start()
        }
    }

    /** [CameraX] ?????? ?????? */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            processCameraProvider = cameraProviderFuture.get()
            bindCameraUseCase()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCase() {
        val cameraProvider = processCameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        val screenAspectRatio = AspectRatio.RATIO_4_3

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()

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
            camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPreviewViewOnTouchListenerForPoseImage() {
        // ??????, ??????
        var scaleFactor = 1.0f
        poseImageScaleGestureDetector = ScaleGestureDetector(requireContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // scaleFactor ??????: [POSE_MIN_SCALE] ~ [POSE_MAX_SCALE]
                val nextScaleFactor = scaleFactor*(detector?.scaleFactor ?: 1.0f)
                scaleFactor = Math.max(POSE_MIN_SCALE, Math.min(nextScaleFactor, POSE_MAX_SCALE))
                binding.imagePose.scaleX = scaleFactor
                binding.imagePose.scaleY = scaleFactor
                return true
            }
        })

        // ??????
        var prevPointerCount = 0
        var startTouchX = 0.0f
        var startTouchY = 0.0f
        var startPoseImageX = 0.0f
        var startPoseImageY = 0.0f

        binding.previewview.setOnTouchListener { _, event ->
            // ??????, ?????? ?????????
            poseImageScaleGestureDetector.onTouchEvent(event)

            // ???????????? ??????
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTouchX = event.x
                    startTouchY = event.y
                    startPoseImageX = binding.imagePose.x
                    startPoseImageY = binding.imagePose.y
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.pointerCount != prevPointerCount) {
                        prevPointerCount = event.pointerCount
                        startTouchX = event.x
                        startTouchY = event.y
                        startPoseImageX = binding.imagePose.x
                        startPoseImageY = binding.imagePose.y
                    }

                    binding.imagePose.x = startPoseImageX + (event.x - startTouchX)
                    binding.imagePose.y = startPoseImageY + (event.y - startTouchY)
                    binding.imagePose.requestLayout()
                }
            }
            false
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

        orientationEventListener.enable()
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
        orientationEventListener.disable()
    }

    override fun onStop() {
        super.onStop()
        bottomMarginValueAnimator?.cancel()
        shutterObjectAnimator?.cancel()
    }

    override fun onDestroy() {
        _binding = null
        cameraExecutor.shutdown()

        super.onDestroy()
    }


    /** [PreviewFragmentListener] */
    override fun onClickGalleryButton() {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            type = "image/*"
        }
        startActivity(intent)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onClickShutterButton() {
        imageCapture?.let { imageCapture ->
            imageCapture.takePicture(cameraExecutor, object: ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeOptInUsageError")
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    GlobalScope.launch {
                        image.image?.toBitmap()?.rotate(image.imageInfo.rotationDegrees)?.let {
                            viewModel.setLatestImage(it) // latestImage??? ????????? ?????? ???????????? ?????????????????????.
                            saveBitmapInPictureDirectory(it, requireContext())
                        }
                        image.close()
                    }
                }
            })

            startShutterEffect()
        }
    }

    private fun startShutterEffect() {
        shutterObjectAnimator = ObjectAnimator.ofFloat(binding.viewShuttereffect, "alpha", 0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = SHUTTER_EFFECT_DURATION / 2
            startDelay = 500L // ?????? ????????? ????????? ?????? ??????????????? ??????????????? ?????? ???????????? ???????????????.
            start()
            doOnEnd {
                ObjectAnimator.ofFloat(binding.viewShuttereffect, "alpha", 1f, 0f).apply {
                    interpolator = DecelerateInterpolator()
                    duration = SHUTTER_EFFECT_DURATION / 2
                    start()
                }
            }
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
        private const val SHUTTER_EFFECT_DURATION = 400L
        private const val POSE_MIN_SCALE = 0.3f
        private const val POSE_MAX_SCALE = 2.0f
    }
}

interface PreviewFragmentListener {
    fun onClickGalleryButton()
    fun onClickShutterButton()
    fun onClickPosePackButton()
    fun onClickPreview()
    fun onClickSwitchLensFacing()
}