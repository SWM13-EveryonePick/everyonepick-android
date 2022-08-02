package org.soma.everyonepick.login.ui.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common_ui.AnimationUtil
import org.soma.everyonepick.common_ui.AnimationUtil.Companion.startShowingUpAnimation
import org.soma.everyonepick.login.databinding.FragmentLanding1Binding
import org.soma.everyonepick.login.utility.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.login.utility.SHOWING_UP_ANIMATION_Y_OFFSET

class Landing1Fragment : Fragment() {
    private var _binding: FragmentLanding1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanding1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        startShowingUpAnimation(binding.root, SHOWING_UP_ANIMATION_Y_OFFSET, SHOWING_UP_ANIMATION_DURATION)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}