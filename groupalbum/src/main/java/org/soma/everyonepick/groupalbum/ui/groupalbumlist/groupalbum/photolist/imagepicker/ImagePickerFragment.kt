package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.imagepicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.groupalbum.adapter.ImageAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentImagepickerBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment.Companion.URI_LIST_CHECKED
import org.soma.everyonepick.groupalbum.viewmodel.ImagePickerViewModel

class ImagePickerFragment : Fragment() {
    private var _binding: FragmentImagepickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagepickerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.adapter = ImageAdapter(viewModel)
        binding.viewModel = viewModel

        viewModel.fetchImageItemList(requireContext())

        (activity as HomeActivityUtility).hideBottomNavigationView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivityUtility).showBottomNavigationView()
    }


    fun onClickUploadButton() {
        activity?.supportFragmentManager?.setFragmentResult(
            URI_LIST_CHECKED,
            bundleOf("uriList" to viewModel.getCheckedImageUriList())
        )
        findNavController().navigateUp()
    }
}