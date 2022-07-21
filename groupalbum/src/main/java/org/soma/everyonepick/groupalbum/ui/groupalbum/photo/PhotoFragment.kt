package org.soma.everyonepick.groupalbum.ui.groupalbum.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPhotoBinding
import org.soma.everyonepick.groupalbum.viewmodel.PhotoViewModel

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickBackButton() {
        findNavController().navigateUp()
    }
}