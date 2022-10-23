package org.soma.everyonepick.common_ui.util

import android.graphics.*
import android.media.Image
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class ImageUtil {
    companion object {
        fun Image.toByteArray(): ByteArray {
            val planeProxy = planes[0]
            val buffer: ByteBuffer = planeProxy.buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            return bytes
        }

        fun Image.toBitmap(): Bitmap {
            val bytes = toByteArray()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun Bitmap.rotate(degree: Int): Bitmap {
            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(degree.toFloat())
            return Bitmap.createBitmap(this, 0, 0, width, height, rotateMatrix, false)
        }
    }
}