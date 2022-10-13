package org.soma.everyonepick.login.ui.landing.viewpagerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.common_ui.util.AnimationUtil.Companion.startShowingUpAnimation
import org.soma.everyonepick.login.databinding.FragmentLanding2Binding
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_Y_OFFSET

class Landing2Fragment : Fragment() {
    private var _binding: FragmentLanding2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanding2Binding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        startShowingUpAnimation(binding.root, SHOWING_UP_ANIMATION_Y_OFFSET, SHOWING_UP_ANIMATION_DURATION)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    companion object {
        @JvmStatic
        fun newInstance() = Landing2Fragment()
    }
}