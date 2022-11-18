package org.soma.everyonepick.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.common.util.CONTACT_EMAIL_ADDRESS
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.setting.R
import org.soma.everyonepick.setting.databinding.FragmentSettingBinding
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(), SettingFragmentListener {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by viewModels()

    @Inject lateinit var dataStoreUseCase: DataStoreUseCase
    @Inject lateinit var userUseCase: UserUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.listener = this
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).showBottomNavigationView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** SettingFragmentListener */
    override fun onClickAccountSettingButton() {
        val directions = SettingFragmentDirections.toAccountSettingFragment()
        findNavController().navigate(directions)
    }

    override fun onClickNotificationSettingButton() {
        /* TODO: enable it after notification settings implemented
        val directions = SettingFragmentDirections.toNotificationSettingFragment()
        findNavController().navigate(directions)
        */
    }

    override fun onClickTermsButton() {
        val directions = SettingFragmentDirections.toTermsFragment()
        findNavController().navigate(directions)
    }

    override fun onClickContactButton() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(CONTACT_EMAIL_ADDRESS))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_subject))
        }
        startActivity(Intent.createChooser(intent, getString(R.string.contact_chooser_text)))
    }
}

interface SettingFragmentListener {
    fun onClickAccountSettingButton()
    fun onClickNotificationSettingButton()
    fun onClickTermsButton()
    fun onClickContactButton()
}