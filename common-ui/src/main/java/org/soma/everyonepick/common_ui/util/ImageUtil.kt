package org.soma.everyonepick.common_ui.util

import android.graphics.*
import android.media.Image
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

class ImageUtil {
    companion object {
        fun Image.toByteArray(): ByteArray {
            val buffer = planes[0].buffer
            buffer.rewind()
            return ByteArray(buffer.capacity())
        }

        fun Image.toBitmap(): Bitmap {
            val buffer = planes[0].buffer
            buffer.rewind()
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun Bitmap.rotate(degree: Int): Bitmap {
            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(degree.toFloat())
            return Bitmap.createBitmap(this, 0, 0, width, height, rotateMatrix, false)
        }
    }
}