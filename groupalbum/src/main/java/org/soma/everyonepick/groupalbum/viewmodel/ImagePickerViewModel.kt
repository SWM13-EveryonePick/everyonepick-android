package org.soma.everyonepick.groupalbum.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.domain.model.ImageModel
import java.io.File

class ImagePickerViewModel: ViewModel() {
    val imageItemList = MutableLiveData<MutableList<ImageModel>>(mutableListOf())

    @SuppressLint("Range")
    fun fetchImageModelList(context: Context) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            INDEX_MEDIA_ID,
            INDEX_MEDIA_URI,
            INDEX_ALBUM_NAME,
            INDEX_DATE_ADDED
        )
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null
        val sortOrder = "$INDEX_DATE_ADDED DESC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.let {
            while(cursor.moveToNext()) {
                val mediaPath = cursor.getString(cursor.getColumnIndex(INDEX_MEDIA_URI))
                imageItemList.value!!.add(ImageModel(Uri.fromFile(File(mediaPath)), false))
                imageItemList.postValue(imageItemList.value)
            }
        }

        cursor?.close()
    }


    fun getCheckedImageUriList(): MutableList<String> {
        val checkedImageUriList = mutableListOf<String>()
        imageItemList.value?.let {
            for(imageItem in it) {
                if (imageItem.isChecked) checkedImageUriList.add(imageItem.uri.toString())
            }
        }
        return checkedImageUriList
    }

    companion object {
        private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
        private const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
        private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED
    }
}