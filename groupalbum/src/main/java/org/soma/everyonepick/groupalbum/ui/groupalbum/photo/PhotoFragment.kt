package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoBinding
import org.soma.everyonepick.groupalbum.viewmodel.PhotoViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by viewModels()
    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel

        viewModel.photoUrl.value = args.photoUrl

        (activity as HomeActivityUtility).hideBottomNavigationView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        (activity as HomeActivityUtility).showBottomNavigationView()
    }


    /** Databinding functions */
    fun onClickBackButton() {
        findNavController().navigateUp()
    }

    fun onClickSaveButton() {
        val appName = getString(org.soma.everyonepick.common.R.string.app_name)
        val fileName = getFileName(appName)
        val bitmap = binding.image.drawable.toBitmap()
        saveBitmapInPictureDirectoryChild(fileName, appName, bitmap)
    }

    private fun getFileName(appName: String): String {
        val calendar = Calendar.getInstance()
        return "$appName" +
                "_${calendar.get(Calendar.YEAR)}${calendar.get(Calendar.MONTH)}${calendar.get(Calendar.DAY_OF_MONTH)}" +
                "_${calendar.get(Calendar.HOUR_OF_DAY)}${calendar.get(Calendar.MINUTE)}${calendar.get(Calendar.SECOND)}" +
                "_${calendar.get(Calendar.MILLISECOND)}"
    }

    private fun saveBitmapInPictureDirectoryChild(fileName: String, child: String, bitmap: Bitmap) {
        try {
            val imageRoot = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                child
            )
            imageRoot.mkdir()
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

    fun onClickDeleteButton() {
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
}