package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.photo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common_ui.util.FileUtil.Companion.getUriFromBitmap
import org.soma.everyonepick.common_ui.util.FileUtil.Companion.saveBitmapInPictureDirectory
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment(), PhotoFragmentListener {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.listener = this
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** PhotoFragmentListener */
    override fun onClickSaveButton() {
        val bitmap = binding.imagePhoto.drawable.toBitmap()
        saveBitmapInPictureDirectory(bitmap, requireContext(), lifecycleScope, showsToast = true)
    }

    override fun onClickDeleteButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage("사진을 삭제합니다.")
            .setPositiveButtonText("삭제")
            .setOnClickPositiveButton {
                // TODO: API Call
                findNavController().navigateUp()
            }
            .build().show()
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
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
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