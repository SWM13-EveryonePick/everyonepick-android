package org.soma.everyonepick.common_ui.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class FileUtil {
    companion object {
        private const val TAG = "FileUtil"

        /**
         * [Bitmap]을 [getImageFile]로 얻은 이미지 파일 경로에 png 형태로 저장합니다. 이 함수는 초단위의 연산 작업을
         * 할 수 있기 때문에 Main Thread에서 실행하지 않는 것을 권장합니다.
         */
        fun saveBitmapInPictureDirectory(
            bitmap: Bitmap,
            context: Context,
            onSuccess: () -> Unit = {},
            onFailure: () -> Unit = {},
        ) {
            try {
                val imageFile = getImageFile(context)
                val outputStream: OutputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // 저장한 파일을 스캔하여 해당 사진이 갤러리에서 보여지게끔 합니다.
                // 아래 코드가 없을 경우, 사진을 저장하여도 곧바로 갤러리 앱에서 해당 사진을 찾아볼 수 없습니다.
                MediaScannerConnection.scanFile(
                    context, arrayOf(imageFile.path), null, null)

                onSuccess.invoke()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                onFailure.invoke()
            }
        }

        /**
         * 아래 예시와 같은 경로의 [File]을 반환합니다.
         * e.g) [Environment.DIRECTORY_PICTURES]\AppName\AppName_202281_104456_691.png
         */
        private fun getImageFile(context: Context): File {
            val folderName = getAppName(context)
            val imageRoot = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                folderName
            )
            imageRoot.mkdirs()
            return File(imageRoot, "${getFileName(context)}.png")
        }

        private fun getAppName(context: Context) = context.getString(org.soma.everyonepick.common_ui.R.string.app_name)

        private fun getFileName(context: Context) = "${getAppName(context)}_${Calendar.getInstance().toFileString()}"

        private fun Calendar.toFileString() =
            "${get(Calendar.YEAR)}${get(Calendar.MONTH)}${get(Calendar.DAY_OF_MONTH)}" +
                "_${get(Calendar.HOUR_OF_DAY)}${get(Calendar.MINUTE)}${get(Calendar.SECOND)}" +
                "_${get(Calendar.MILLISECOND)}"


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