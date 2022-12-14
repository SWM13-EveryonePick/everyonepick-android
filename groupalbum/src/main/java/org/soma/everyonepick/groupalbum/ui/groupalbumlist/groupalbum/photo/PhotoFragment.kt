package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.util.FileUtil.Companion.getUriFromBitmap
import org.soma.everyonepick.common_ui.util.FileUtil.Companion.saveBitmapInPictureDirectory
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoBinding

@AndroidEntryPoint
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
        _binding = null
        super.onDestroy()
    }


    /** PhotoFragmentListener */
    @OptIn(DelicateCoroutinesApi::class)
    override fun onClickSaveButton() {
        GlobalScope.launch {
            val bitmap = binding.imagePhoto.drawable.toBitmap()
            saveBitmapInPictureDirectory(
                bitmap,
                requireContext(), {
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), requireContext().getString(org.soma.everyonepick.common_ui.R.string.toast_save_image_success), Toast.LENGTH_SHORT).show()
                    }
                }, {
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), requireContext().getString(org.soma.everyonepick.common_ui.R.string.toast_failed_to_save_image), Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    override fun onClickDeleteButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_delete_photo))
            .setPositiveButtonText(getString(org.soma.everyonepick.common_ui.R.string.delete))
            .setOnClickPositiveButton {
                viewModel.deletePhotoOrResultPhoto(
                    { findNavController().navigateUp() },
                    { Toast.makeText(requireContext(), getString(R.string.toast_failed_to_delete_photo), Toast.LENGTH_SHORT).show() }
                )
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
        startActivity(Intent.createChooser(intent, getString(R.string.share_photo)))
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