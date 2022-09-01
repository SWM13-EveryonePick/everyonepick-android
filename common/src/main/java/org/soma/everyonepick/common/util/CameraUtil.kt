package org.soma.everyonepick.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image

class CameraUtil {
    companion object {
        fun Image.toBitmap(): Bitmap {
            val buffer = planes[0].buffer
            buffer.rewind()
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}