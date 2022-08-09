package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.photo

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoBinding
import org.soma.everyonepick.groupalbum.util.FileUtil
import org.soma.everyonepick.groupalbum.util.FileUtil.Companion.getFileName
import org.soma.everyonepick.groupalbum.util.FileUtil.Companion.getUriFromBitmap
import org.soma.everyonepick.groupalbum.util.FileUtil.Companion.saveBitmapInPictureDirectory
import org.soma.everyonepick.groupalbum.util.FileUtil.Companion.toFileString
import org.soma.everyonepick.groupalbum.util.FileUtil.Companion.toString
import org.soma.everyonepick.groupalbum.viewmodel.PhotoViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class PhotoFragment : Fragment(), PhotoFragmentListener {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by viewModels()
    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.listener = this
        }

        viewModel.photoUrl.value = args.photoUrl

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** PhotoFragmentListener */
    override fun onClickSaveButton() {
        val appName = getString(org.soma.everyonepick.common.R.string.app_name)
        val fileName = getFileName(appName)
        val bitmap = binding.imagePhoto.drawable.toBitmap()
        saveBitmapInPictureDirectory(requireContext(), fileName, appName, bitmap)
    }

    override fun onClickDeleteButton() {
        AlertDialog.Builder(context).setMessage("사진을 삭제합니다.")
            .setPositiveButton("확인") { _, _ ->
                // TODO: API Call
                findNavController().navigateUp()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    override fun onClickShareButton() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            val bitmap = binding.imagePhoto.drawable.toBitmap()
            val uri = getUriFromBitmap(requireContext(), bitmap)
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "사진 공유"))
    }

    override fun onClickAddToStoryButton() {
        val intent = Intent("com.instagram.share.ADD_TO_FEED").apply {
            val bitmap = binding.imagePhoto.drawable.toBitmap()
            val uri = getUriFromBitmap(requireContext(), bitmap)
            setDataAndType(uri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }
}

interface PhotoFragmentListener {
    fun onClickSaveButton()
    fun onClickDeleteButton()
    fun onClickShareButton()
    fun onClickAddToStoryButton()
}