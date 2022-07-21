package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.PhotoAdapter
import org.soma.everyonepick.groupalbum.data.PhotoDao
import org.soma.everyonepick.groupalbum.data.PhotoItem
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotolistBinding
import org.soma.everyonepick.groupalbum.utility.PhotoListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel
import org.soma.everyonepick.groupalbum.viewmodel.PhotoListViewModel

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickUploadPhotoButton() {
        // TODO: 사진 업로드로 대체
        val id = viewModel.photoItemList.value!!.size.toLong()
        viewModel.addPhotoItem(PhotoItem(
            PhotoDao(id, "https://picsum.photos/200"),
            false,
            false
        ))
    }

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