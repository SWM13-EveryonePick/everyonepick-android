package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.os.Bundle
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
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.imagepicker.ImagePickerFragment
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick.PickFragmentType
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
                        if (it.id != null && it.id != GroupAlbum.dummyData.id)
                            viewModel.readPhotoModelList(it.id)
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
                    val requestBody = inputStream!!.readBytes().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("images", File(it).name, requestBody)
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
        _binding = null
        super.onDestroy()
    }


    /** PhotoListFragmentListener */
    override fun onClickUploadPhotoButton() {
        val directions = GroupAlbumFragmentDirections.toParentPermissionFragment()
        findNavController().navigate(directions)
    }

    override fun onClickDeleteButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_delete_photo))
            .setOnClickPositiveButton {
                viewModel.deleteCheckedPhotoList(parentViewModel.groupAlbum.value.id)
                parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
            }
            .build().show()
    }

    override fun onClickProcessButton() {
        lifecycleScope.launch {
            val hasSyntheticDialogShown = dataStoreUseCase.hasSyntheticDialogShown.first()
            if (hasSyntheticDialogShown != true) {
                DialogWithTwoButton.Builder(requireContext())
                    .setMessage(getString(R.string.dialog_synthetic))
                    .setPositiveButtonText(getString(org.soma.everyonepick.common_ui.R.string.confirm))
                    .setOnClickPositiveButton {
                        checkCheckedPhotoCountAndNavigate()
                    }
                    .build().show()
                dataStoreUseCase.editHasSyntheticDialogShown(true)
            } else {
                checkCheckedPhotoCountAndNavigate()
            }
        }
    }

    private fun checkCheckedPhotoCountAndNavigate() {
        if (viewModel.getCheckedPhotoList().count() <= SYNTHESIS_MAX_PHOTO_COUNT) {
            navigateToPickFragment()
        } else {
            Toast.makeText(requireContext(), getString(R.string.toast_exceed_synthesis_max_photo_count, SYNTHESIS_MAX_PHOTO_COUNT), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToPickFragment() {
        val checkedPhotoList = viewModel.getCheckedPhotoList()
        val directions = GroupAlbumFragmentDirections.toPickFragment(
            parentViewModel.groupAlbum.value.id?: -1,
            checkedPhotoList.map { it.id }.toLongArray(),
            checkedPhotoList.map { it.photoUrl }.toTypedArray(),
            PickFragmentType.TO_CREATE,
            -1L
        )

        findNavController().navigate(directions)
    }

    override fun onClickCancelButton() {
        parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }


    companion object {
        const val URI_LIST_CHECKED_REQUEST_KEY = "uri_list_checked_request_key"
        const val URI_LIST_CHECKED_KEY = "uri_list_checked_key"
        const val SYNTHESIS_MAX_PHOTO_COUNT = 10 // 합성 시 최대 선택 가능한 장수

        @JvmStatic
        fun newInstance() = PhotoListFragment()
    }
}

interface PhotoListFragmentListener {
    fun onClickUploadPhotoButton()
    fun onClickDeleteButton()
    fun onClickProcessButton()
    fun onClickCancelButton()
}