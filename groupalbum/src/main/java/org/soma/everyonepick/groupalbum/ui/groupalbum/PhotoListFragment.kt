package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.PhotoAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotolistBinding
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

        val adapter = PhotoAdapter()
        binding.recyclerviewPhoto.adapter = adapter
        viewModel.updatePhotoItemList(parentViewModel.groupAlbum.value!!.id)

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: PhotoAdapter) {
        viewModel.photoItemList.observe(viewLifecycleOwner) { photoItemList ->
            adapter.submitList(photoItemList.toMutableList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}