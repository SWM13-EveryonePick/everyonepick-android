package org.soma.everyonepick.setting.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.setting.R
import org.soma.everyonepick.setting.databinding.FragmentAccountSettingBinding

class AccountSettingFragment : Fragment(), AccountSettingFragmentListener {
    private var _binding: FragmentAccountSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSettingBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.listener = this
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** AccountSettingFragmentListener */
    override fun onClickLogoutButton() {
        // TODO: 로그아웃
    }
    override fun onClickLeaveButton() {
        // TODO: 탈퇴
    }
}

interface AccountSettingFragmentListener {
    fun onClickLogoutButton()
    fun onClickLeaveButton()
}