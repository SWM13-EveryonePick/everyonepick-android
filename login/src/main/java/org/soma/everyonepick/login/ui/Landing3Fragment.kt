package org.soma.everyonepick.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.FragmentLanding3Binding

class Landing3Fragment : Fragment() {
    private var _binding: FragmentLanding3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanding3Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickNextButton() {
        findNavController().navigate(
            Landing3FragmentDirections.actionLanding3ToLogin()
        )
    }
}