package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.ImageAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentImagepickerBinding
import org.soma.everyonepick.groupalbum.viewmodel.ImagePickerViewModel

class ImagePickerFragment : Fragment() {
    private var _binding: FragmentImagepickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagepickerBinding.inflate(inflater, container, false)

        val adapter = ImageAdapter()
        binding.recyclerviewImage.adapter = adapter

        viewModel.fetchImageItemList(requireContext())
        subscribeUi(adapter)

        (activity as HomeActivityUtility).hideBottomNavigationView()

        return binding.root
    }

    private fun subscribeUi(adapter: ImageAdapter) {
        viewModel.imageItemList.observe(viewLifecycleOwner) { imageItemList ->
            adapter.submitList(imageItemList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivityUtility).showBottomNavigationView()
    }
}