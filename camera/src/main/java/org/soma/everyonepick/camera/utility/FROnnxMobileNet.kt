package org.soma.everyonepick.camera.utility

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtLoggingLevel
import ai.onnxruntime.OrtSession
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.soma.everyonepick.camera.CameraFragment
import org.soma.everyonepick.camera.R
import java.io.ByteArrayOutputStream
import java.nio.FloatBuffer
import java.util.*

typealias OnnxListener = (FloatArray) -> Unit

class FROnnxMobileNet(private val context: Context, listener: OnnxListener): ImageAnalysis.Analyzer {
    private val listeners = ArrayList<OnnxListener>().apply { listener?.let { add(it) } }

    // 안드로이드 가속화
    private var enableNNAPI: Boolean = false

    // 오닉스 런타임 (ONNX)
    private var ortEnv: OrtEnvironment? = null
    private var ortSession: OrtSession? = null

    init {
        ortEnv = OrtEnvironment.getEnvironment(OrtLoggingLevel.ORT_LOGGING_LEVEL_FATAL)
        ortSession = createOrtSession()
    }

    private fun createOrtSession(): OrtSession? {
        val so = OrtSession.SessionOptions()
        so.use {
            // Set to use 2 intraOp threads for CPU EP
            so.setIntraOpNumThreads(2)

            if (enableNNAPI) so.addNnapi()

            return ortEnv?.createSession(readModel(), so)
        }
    }

    private fun readModel() = context.resources.openRawResource(R.raw.mobileface).readBytes()

    private fun preprocess2(bitmap: Bitmap): FloatBuffer {
        val imgData = FloatBuffer.allocate(
            DIM_BATCH_SIZE
                    * IMAGE_SIZE_X
                    * IMAGE_SIZE_Y
                    * DIM_PIXEL_SIZE)
        imgData.rewind()

        val bmpData = IntArray(IMAGE_SIZE_X * IMAGE_SIZE_Y)
        bitmap.getPixels(bmpData, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var idx = 0
        for (i in 0 until IMAGE_SIZE_X) {
            for (j in 0 until IMAGE_SIZE_Y) {
                val pixelValue = bmpData[idx++]
                imgData.put(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.put(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.put(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }

        imgData.rewind()
        return imgData
    }

    fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val vuBuffer = planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun analyze(image: ImageProxy) {
        if(listeners.isEmpty()) {
            image.close()
            return
        }

        val bitmap = image.toBitmap()
        val resizeBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, false)
        val imgData = preprocess2(resizeBitmap) //128x128 => 49152

        val inputName = ortSession?.inputNames?.iterator()?.next()

        val shape = longArrayOf(1, 3, 128, 128)
        // val shape = longArrayOf(3, 224, 224)
        val ortEnv = OrtEnvironment.getEnvironment()
        ortEnv.use {
            // Create input tensor
            val inputTensor = OnnxTensor.createTensor(ortEnv, imgData, shape)
            val startTime = SystemClock.uptimeMillis()
            inputTensor.use {
                // Run the inference and get the output tensor
                val output = ortSession?.run(Collections.singletonMap(inputName, inputTensor))
                // val output = ortSession?.run(Collections.singletonMap("input", input_tensor))
                output.use {
                    // reshape(-1)
                    val output0 = (output?.get(0)?.value) as Array<Array<Array<FloatArray>>>
                    val output1 = output0[0].flatten()
                    output1[0].forEach { }
                    val output2 = FloatArray(output1.size)
                    output1.forEachIndexed { index, floats ->
                        floats.forEach {
                            output2[index] = it
                        }
                    }

                    listeners.forEach { it(output2) }

                    output.close()
                }
            }
        }

        image.close()
    }

    companion object {
        const val IMAGE_MEAN: Float = .0f
        const val IMAGE_STD: Float = 255f
        const val DIM_BATCH_SIZE = 1
        const val DIM_PIXEL_SIZE = 3
        const val IMAGE_SIZE_X = 128
        const val IMAGE_SIZE_Y = 128
    }
}