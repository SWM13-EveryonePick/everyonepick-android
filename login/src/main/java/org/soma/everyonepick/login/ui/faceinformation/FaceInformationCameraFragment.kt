package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.login.databinding.FragmentFaceInformationCameraBinding

class FaceInformationCameraFragment : Fragment() {
    private var _binding: FragmentFaceInformationCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationCameraBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // TODO: 얼굴 인식 성공하면 자동으로 넘어가기
    fun onClickNextButton() {
        val directions = FaceInformationCameraFragmentDirections.toFaceInformationCompleteFragment()
        findNavController().navigate(directions)
    }
}