package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.util.AnimationUtil
import org.soma.everyonepick.login.databinding.FragmentFaceInformationCompleteBinding
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_Y_OFFSET
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class FaceInformationCompleteFragment : Fragment() {
    private var _binding: FragmentFaceInformationCompleteBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var homeActivityClass: Class<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationCompleteBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.onClickNextButton = View.OnClickListener {
                LoginUtil.startHomeActivity(requireActivity(), homeActivityClass)
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