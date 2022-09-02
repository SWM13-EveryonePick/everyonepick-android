package org.soma.everyonepick.setting.ui.terms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.FullTextActivity
import org.soma.everyonepick.common_ui.FullTextActivity.Companion.putFullTextActivityExtras
import org.soma.everyonepick.common_ui.R
import org.soma.everyonepick.setting.databinding.FragmentTermsBinding

class TermsFragment : Fragment(), TermsFragmentListener {
    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.listener = this
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** TermsFragmentListener */
    override fun onClickTermsOfServiceButton() {
        val intent = Intent(requireContext(), FullTextActivity::class.java)
            .putFullTextActivityExtras(getString(R.string.terms_of_service_title), getString(R.string.terms_of_service_contents))
        startActivity(intent)
    }

    override fun onClickPrivacyPolicyButton() {
        val intent = Intent(requireContext(), FullTextActivity::class.java)
            .putFullTextActivityExtras(getString(R.string.privacy_policy_title), getString(R.string.privacy_policy_contents))
        startActivity(intent)
    }
}

interface TermsFragmentListener {
    fun onClickTermsOfServiceButton()
    fun onClickPrivacyPolicyButton()
}