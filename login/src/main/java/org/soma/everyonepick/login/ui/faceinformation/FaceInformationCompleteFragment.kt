package org.soma.everyonepick.login.ui.faceinformation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.FragmentFaceInformationCompleteBinding

class FaceInformationCompleteFragment : Fragment() {
    private var _binding: FragmentFaceInformationCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceInformationCompleteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickNextButton() {
        startHomeActivity()
    }

    private fun startHomeActivity() {
        val intent = Intent(
            requireContext(),
            Class.forName(HOME_ACTIVITY_CLASS)
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}