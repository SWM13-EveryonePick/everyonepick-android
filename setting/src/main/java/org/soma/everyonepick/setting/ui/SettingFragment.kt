package org.soma.everyonepick.setting.ui

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
import org.soma.everyonepick.common.util.HomeActivityUtil
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
            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.listener = this
        }

        lifecycleScope.launch {
            viewModel.isApiLoading.value = true
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                viewModel.me.value = userUseCase.readUser(token)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "회원 정보를 불러오는 데 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
            viewModel.isApiLoading.value = true
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).showBottomNavigationView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** SettingFragmentListener */
    override fun onClickAccountSettingButton() {
        val directions = SettingFragmentDirections.toAccountSettingFragment()
        findNavController().navigate(directions)
    }

    override fun onClickNotificationSettingButton() {
        val directions = SettingFragmentDirections.toNotificationSettingFragment()
        findNavController().navigate(directions)
    }

    override fun onClickTermsButton() {
        val directions = SettingFragmentDirections.toTermsFragment()
        findNavController().navigate(directions)
    }

    override fun onClickContactButton() {
        // TODO: mail action?
    }
}

interface SettingFragmentListener {
    fun onClickAccountSettingButton()
    fun onClickNotificationSettingButton()
    fun onClickTermsButton()
    fun onClickContactButton()
}