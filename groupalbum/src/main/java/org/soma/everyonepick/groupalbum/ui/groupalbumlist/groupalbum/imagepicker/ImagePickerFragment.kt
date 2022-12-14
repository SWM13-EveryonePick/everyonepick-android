package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.imagepicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentImagePickerBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment.Companion.URI_LIST_CHECKED_KEY
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment.Companion.URI_LIST_CHECKED_REQUEST_KEY

class ImagePickerFragment : Fragment() {
    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = ImageAdapter()
            it.onClickUploadButtonListener = View.OnClickListener {
                activity?.supportFragmentManager?.setFragmentResult(
                    URI_LIST_CHECKED_REQUEST_KEY,
                    bundleOf(URI_LIST_CHECKED_KEY to viewModel.getCheckedImageUriList())
                )
                findNavController().navigateUp()
            }
        }

        viewModel.readImageModelList(requireContext())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}