package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.PhotoAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.utility.PhotoListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel
import org.soma.everyonepick.groupalbum.viewmodel.PhotoListViewModel


@AndroidEntryPoint
class PhotoListFragment: Fragment() {
    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = PhotoAdapter(viewModel)
            it.parentViewModel = parentViewModel
            it.listener = object : PhotoListFragmentListener {
                override fun onClickUploadPhotoButton() {
                    val directions = GroupAlbumViewPagerFragmentDirections.toParentPermissionFragment()
                    findNavController().navigate(directions)
                }

                override fun onClickDeleteButton() {
                    viewModel.deleteCheckedItems()
                    parentViewModel.photoListMode.value = PhotoListMode.NORMAL_MODE.ordinal
                }

                override fun onClickProcessButton() {
                    // TODO: 합성 플로우
                }

                override fun onClickCancelButton() {
                    parentViewModel.photoListMode.value = PhotoListMode.NORMAL_MODE.ordinal
                }
            }
        }

        viewModel.fetchPhotoItemList(parentViewModel.groupAlbum.value!!.id)

        subscribeUi()
        setFragmentResultListeners()

        return binding.root
    }

    private fun subscribeUi() {
        parentViewModel.photoListMode.observe(viewLifecycleOwner) { photoListMode ->
            viewModel.setIsCheckboxVisible(photoListMode == PhotoListMode.SELECTION_MODE.ordinal)
        }
    }

    // ImagePicker에서 선택한 Uri 리스트를 받고 처리합니다.
    private fun setFragmentResultListeners() {
        activity?.supportFragmentManager?.setFragmentResultListener(URI_LIST_CHECKED, viewLifecycleOwner) { _, bundle ->
            bundle.getStringArrayList("uriList")?.let { uriList ->
                for(uri in uriList) {
                    // TODO: 업로드 -> 성공 -> viewModel.fetchPhotoItemList(parentViewModel.groupAlbum.value!!.id) 다시 로드
                    // 또는 1초에 한번씩 viewModel.fetchPhotoItemList(parentViewModel.groupAlbum.value!!.id) 호출 -> 성공 -> ToastMassage
                }
            }
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


    companion object {
        const val URI_LIST_CHECKED = "uri_list_checked"
    }

    interface PhotoListFragmentListener {
        fun onClickUploadPhotoButton()
        fun onClickDeleteButton()
        fun onClickProcessButton()
        fun onClickCancelButton()
    }
}