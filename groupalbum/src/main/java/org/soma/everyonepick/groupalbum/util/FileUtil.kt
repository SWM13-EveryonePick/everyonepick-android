package org.soma.everyonepick.groupalbum.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class FileUtil {
    companion object {
        fun getFileName(appName: String) = "${appName}_${Calendar.getInstance().toFileString()}"

        private fun Calendar.toFileString() = "${get(Calendar.YEAR)}${get(Calendar.MONTH)}${get(Calendar.DAY_OF_MONTH)}" +
                "_${get(Calendar.HOUR_OF_DAY)}${get(Calendar.MINUTE)}${get(Calendar.SECOND)}" +
                "_${get(Calendar.MILLISECOND)}"

        fun saveBitmapInPictureDirectory(context: Context, fileName: String, folderName: String, bitmap: Bitmap) {
            try {
                val imageRoot = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    folderName
                )
                imageRoot.mkdirs()
                val filePath = File(imageRoot, "$fileName.png")

                val outputStream: OutputStream = FileOutputStream(filePath)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                Toast.makeText(context, "이미지를 갤러리에 저장했습니다!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        fun getUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
            val path = File(context.cacheDir, "images")
            path.mkdirs()

            val file = File(path.path + "/to_get_uri.jpg")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()

            return FileProvider.getUriForFile(context, "org.soma.everyonepick.fileprovider", file)
        }
    }
}