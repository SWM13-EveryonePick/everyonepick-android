package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.login.databinding.FragmentFaceInformationCameraBinding
import javax.inject.Inject

@AndroidEntryPoint
class FaceInformationCameraFragment : Fragment() {
    private var _binding: FragmentFaceInformationCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationCameraBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun navigateToFaceInformationCompleteFragment() {
        val directions = FaceInformationCameraFragmentDirections.toFaceInformationCompleteFragment()
        findNavController().navigate(directions)
    }
}