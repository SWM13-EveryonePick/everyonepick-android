package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.imagepicker.ImagePickerFragment
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
            it.adapter = PhotoAdapter()
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
                        try {
                            if (it.id != User.dummyData.id) viewModel.readPhotoModelList(it.id!!)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "정보를 불러오는 데 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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
                for (uri in uriList) {
                    // TODO: 함수 추가: 업로드 -> 성공 -> viewModel.readPhotoModelList(parentViewModel.groupAlbum.value!!.id) 다시 로드?
                }
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
        viewModel.deleteCheckedItems()
        parentViewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onClickProcessButton() {
        lifecycleScope.launch {
            val hasShownSyntheticDialog = dataStoreUseCase.hasShownSyntheticDialog.first()
            if (hasShownSyntheticDialog != true) {
                DialogWithTwoButton.Builder(requireContext())
                    .setMessage("아래와 같은 사항을 지켜야 좋은 사진이 나올 수 있어요!\n\n1. 모든 사진에 모든 멤버들이 포함되어야 해요.\n 2. 마스크를 끼거나 얼굴이 많이 가려진 경우 인식이 힘들어요.\n 3. 유사 구도의 사진들을 선택해주세요.")
                    .setPositiveButtonText("확인")
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