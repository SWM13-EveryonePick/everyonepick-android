package org.soma.everyonepick.login.ui.faceinformation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.soma.everyonepick.common_ui.AnimationUtil
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.databinding.FragmentFaceInformationCompleteBinding
import org.soma.everyonepick.login.utility.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.login.utility.SHOWING_UP_ANIMATION_Y_OFFSET
import org.soma.everyonepick.login.utility.Util

class FaceInformationCompleteFragment : Fragment() {
    private var _binding: FragmentFaceInformationCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationCompleteBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.onClickNextButton = View.OnClickListener {
                Util.startHomeActivity(requireActivity())
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