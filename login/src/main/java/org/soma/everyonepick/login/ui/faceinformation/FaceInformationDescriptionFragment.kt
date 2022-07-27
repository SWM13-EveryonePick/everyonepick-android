package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.login.databinding.FragmentFaceInformationDescriptionBinding

class FaceInformationDescriptionFragment : Fragment() {
    private var _binding: FragmentFaceInformationDescriptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationDescriptionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onClickNextButton = View.OnClickListener {
                findNavController().navigate(FaceInformationDescriptionFragmentDirections.toFaceInformationCameraFragment())
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}