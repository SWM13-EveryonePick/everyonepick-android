package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.PhotoAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotolistBinding
import org.soma.everyonepick.groupalbum.utility.PhotoListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel
import org.soma.everyonepick.groupalbum.viewmodel.PhotoListViewModel
import java.io.File


@AndroidEntryPoint
class PhotoListFragment: Fragment() {
    private var _binding: FragmentPhotolistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotolistBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.parentViewModel = parentViewModel
        binding.viewModel = viewModel

        val adapter = PhotoAdapter(viewModel)
        binding.recyclerviewPhoto.adapter = adapter
        viewModel.fetchPhotoItemList(parentViewModel.groupAlbum.value!!.id)

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: PhotoAdapter) {
        viewModel.photoItemList.observe(viewLifecycleOwner) { photoItemList ->
            adapter.submitList(photoItemList.toMutableList())
        }

        parentViewModel.photoListMode.observe(viewLifecycleOwner) { photoListMode ->
            viewModel.setIsCheckboxVisible(photoListMode == PhotoListMode.SELECTION_MODE.ordinal)
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: viewModel.fetchPhotoItemList(parentViewModel.groupAlbum.value!!.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /*private val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
    private val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
    private val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED*/

    fun onClickUploadPhotoButton() {
        // val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    fun onClickDeleteButton() {
        viewModel.deleteCheckedItems()
        parentViewModel.photoListMode.value = PhotoListMode.NORMAL_MODE.ordinal
    }

    fun onClickProcessButton() {
        // TODO: 합성 플로우
    }

    fun onClickCancelButton() {
        parentViewModel.photoListMode.value = PhotoListMode.NORMAL_MODE.ordinal
    }
}