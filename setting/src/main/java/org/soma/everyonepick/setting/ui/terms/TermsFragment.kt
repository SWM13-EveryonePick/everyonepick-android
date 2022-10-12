package org.soma.everyonepick.setting.ui.terms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.util.PRIVACY_POLICY_URL
import org.soma.everyonepick.common.util.TERMS_OF_SERVICE_URL
import org.soma.everyonepick.common_ui.ScrollableWebViewActivity
import org.soma.everyonepick.common_ui.ScrollableWebViewActivity.Companion.putScrollableWebViewActivityExtras
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
        _binding = null
        super.onDestroy()
    }


    /** TermsFragmentListener */
    override fun onClickTermsOfServiceButton() {
        val intent = Intent(requireContext(), ScrollableWebViewActivity::class.java)
            .putScrollableWebViewActivityExtras(getString(R.string.terms_of_service_title), TERMS_OF_SERVICE_URL)
        startActivity(intent)
    }

    override fun onClickPrivacyPolicyButton() {
        val intent = Intent(requireContext(), ScrollableWebViewActivity::class.java)
            .putScrollableWebViewActivityExtras(getString(R.string.privacy_policy_title), PRIVACY_POLICY_URL)
        startActivity(intent)
    }
}

interface TermsFragmentListener {
    fun onClickTermsOfServiceButton()
    fun onClickPrivacyPolicyButton()
}