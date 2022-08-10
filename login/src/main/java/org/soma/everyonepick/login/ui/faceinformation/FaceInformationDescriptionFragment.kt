package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common.util.AnimationUtil
import org.soma.everyonepick.login.databinding.FragmentFaceInformationDescriptionBinding
import org.soma.everyonepick.login.util.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.login.util.SHOWING_UP_ANIMATION_Y_OFFSET

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
                val directions = FaceInformationDescriptionFragmentDirections.toFaceInformationCameraFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        AnimationUtil.startShowingUpAnimation(binding.root, SHOWING_UP_ANIMATION_Y_OFFSET, SHOWING_UP_ANIMATION_DURATION)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}