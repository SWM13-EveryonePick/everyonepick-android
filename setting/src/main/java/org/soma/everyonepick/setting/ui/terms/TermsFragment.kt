package org.soma.everyonepick.setting.ui.terms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.setting.R
import org.soma.everyonepick.setting.databinding.FragmentTermsBinding

class TermsFragment : Fragment(), TermsFragmentListener {
    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTermsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.listener = this
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** TermsFragmentListener */
    override fun onClickTermsOfServiceButton() {
        val directions = TermsFragmentDirections.toTermsOfServiceFragment()
        findNavController().navigate(directions)
    }

    override fun onClickPrivacyPolicyButton() {
        val directions = TermsFragmentDirections.toPrivacyPolicyFragment()
        findNavController().navigate(directions)
    }
}

interface TermsFragmentListener {
    fun onClickTermsOfServiceButton()
    fun onClickPrivacyPolicyButton()
}