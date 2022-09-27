package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.imagepicker.ImagePickerFragment
import org.soma.everyonepick.groupalbum.util.SelectionMode
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class PhotoListFragment: Fragment(), PhotoListFragmentListener {
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = PhotoAdapter(parentViewModel)
            it.parentViewModel = parentViewModel
            it.listener = this
        }

        subscribeUi()
        setFragmentResultListener()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    parentViewModel.photoSelectionMode.collectLatest { selectionMode ->
                        viewModel.setIsCheckboxVisible(selectionMode == SelectionMode.SELECTION_MODE.ordinal)
                    }
                }

                launch {
                    parentViewModel.groupAlbum.collect {
                        if (it.id != null && it.id != User.dummyData.id) viewModel.readPhotoModelList(it.id)
                    }
                }

                launch {
                    viewModel.toastMessage.collectLatest {
                        if (it.isNotEmpty()) {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    /**
     * [ImagePickerFragment]에서 선택한 사진들의 Uri 리스트를 받습니다.
     */
    private fun setFragmentResultListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(URI_LIST_CHECKED_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            bundle.getStringArrayList(URI_LIST_CHECKED_KEY)?.let { uriList ->
                val images = uriList.map {
                    val inputStream = requireContext().contentResolver.openInputStream(it.toUri())
                    val responseBody = inputStream!!.readBytes().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("images", File(it).name, responseBody)
                }
                viewModel.createPhotoList(parentViewModel.groupAlbum.value.id, images)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** PhotoListFragmentListener */
    override fun onClickUploadPhotoButton() {
        val directions = GroupAlbumFragmentDirections.toParentPermissionFragment()
        findNavController().navigate(directions)
    }

    override fun onClickDeleteButton() {
        viewModel.deleteCheckedPhotoList(parentViewModel.groupAlbum.value.id)
        parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onClickProcessButton() {
        lifecycleScope.launch {
            val hasShownSyntheticDialog = dataStoreUseCase.hasShownSyntheticDialog.first()
            if (hasShownSyntheticDialog != true) {
                DialogWithTwoButton.Builder(requireContext())
                    .setMessage(getString(R.string.dialog_synthetic))
                    .setPositiveButtonText(getString(org.soma.everyonepick.common_ui.R.string.confirm))
                    .setOnClickPositiveButton {
                        // TODO: 합성 플로우
                    }
                    .build().show()
                dataStoreUseCase.editHasShownSyntheticDialog(true)
            } else {
                // TODO: 합성 플로우
            }
        }
    }

    override fun onClickCancelButton() {
        parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }


    companion object {
        const val URI_LIST_CHECKED_REQUEST_KEY = "uri_list_checked_request_key"
        const val URI_LIST_CHECKED_KEY = "uri_list_checked_key"
    }
}

interface PhotoListFragmentListener {
    fun onClickUploadPhotoButton()
    fun onClickDeleteButton()
    fun onClickProcessButton()
    fun onClickCancelButton()
}