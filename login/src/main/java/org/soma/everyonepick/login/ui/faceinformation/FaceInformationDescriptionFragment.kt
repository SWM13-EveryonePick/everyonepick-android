package org.soma.everyonepick.login.ui.faceinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common_ui.util.AnimationUtil
import org.soma.everyonepick.login.databinding.FragmentFaceInformationDescriptionBinding
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_DURATION
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_Y_OFFSET
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.login.R

class FaceInformationDescriptionFragment : Fragment() {
    private var _binding: FragmentFaceInformationDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceInformationDescriptionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onClickNextButton = View.OnClickListener {
                val directions = FaceInformationDescriptionFragmentDirections.toFaceInformationGuideFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        AnimationUtil.startShowingUpAnimation(binding.root, SHOWING_UP_ANIMATION_Y_OFFSET, SHOWING_UP_ANIMATION_DURATION)
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                DialogWithTwoButton.Builder(requireContext())
                    .setMessage(getString(R.string.dialog_leave_app_to_cancel_to_sign_up))
                    .setPositiveButtonText(getString(org.soma.everyonepick.common_ui.R.string.termination))
                    .setOnClickPositiveButton {
                        activity?.finish()
                    }
                    .build().show()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}