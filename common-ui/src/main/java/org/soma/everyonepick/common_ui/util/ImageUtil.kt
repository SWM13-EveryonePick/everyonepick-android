package org.soma.everyonepick.common_ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image

class ImageUtil {
    companion object {
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