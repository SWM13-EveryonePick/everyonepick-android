package org.soma.everyonepick.setting.ui.account

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.setting.R
import org.soma.everyonepick.setting.databinding.FragmentAccountSettingBinding
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class AccountSettingFragment : Fragment(), AccountSettingFragmentListener {
    private var _binding: FragmentAccountSettingBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

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

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** AccountSettingFragmentListener */
    override fun onClickLogoutButton() {
        AlertDialog.Builder(context).setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("로그아웃") { _, _ ->
                lifecycleScope.launch {
                    dataStoreUseCase.editAccessToken(null)
                    dataStoreUseCase.editRefreshToken(null)
                }
                (activity as HomeActivityUtil).startLoginActivity()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }
    override fun onClickLeaveButton() {
        AlertDialog.Builder(context).setMessage("정말 회원탈퇴를 하시겠습니까?")
            .setPositiveButton("회원탈퇴") { _, _ ->
                // TODO: 회원탈퇴
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }
}

interface AccountSettingFragmentListener {
    fun onClickLogoutButton()
    fun onClickLeaveButton()
}