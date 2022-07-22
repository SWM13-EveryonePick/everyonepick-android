package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.ImageAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentImagepickerBinding

private val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
private val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
private val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
private val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

class ImagePickerFragment : Fragment() {
    private var _binding: FragmentImagepickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagepickerBinding.inflate(inflater, container, false)

        val adapter = ImageAdapter()
        binding.recyclerviewImage.adapter = adapter

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: ImageAdapter) {

    }

    /*@SuppressLint("Range")
    private fun f() {
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
        val cursor =
            requireContext().contentResolver.query(uri, projection, selection, null, sortOrder)

        val albumList = MutableLiveData<MutableList<Uri>>()
        cursor?.let {
            while(cursor.moveToNext()) {
                val mediaPath = cursor.getString(cursor.getColumnIndex(INDEX_MEDIA_URI))
                albumList.value?.add(Uri.fromFile(File(mediaPath)))
                Log.e("ASD", Uri.fromFile(File(mediaPath)).toString())
            }
        }

        cursor?.close()
    }*/
}