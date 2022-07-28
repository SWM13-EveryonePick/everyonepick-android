package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.common_ui.KeyboardUtil
import org.soma.everyonepick.common_ui.getVisibility
import org.soma.everyonepick.common_ui.setVisibility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.InviteFriendAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentInviteFriendBinding
import org.soma.everyonepick.groupalbum.ui.ViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.viewmodel.InviteFriendViewModel

@AndroidEntryPoint
class InviteFriendFragment : Fragment() {
    private var _binding: FragmentInviteFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InviteFriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteFriendBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = InviteFriendAdapter(viewModel)
            it.viewModel = viewModel
            it.onClickNextButtonListener = View.OnClickListener {
                val directions = InviteFriendFragmentDirections.toGroupAlbumTitleFragment(
                    viewModel.getCheckedFriendList().toTypedArray()
                )
                findNavController().navigate(directions)
            }
        }

        // 백스택 시, 체크박스들이 체크되지 않은 상태에서 시작하여 bind() 동작을 수행하게 됩니다.
        // 이때 체크박스가 체크되면서 checked 값도 같이 변하므로, bind() 이전에 checked를 초기화 시켜야 합니다.
        viewModel.checked.value = 0

        KeyboardUtil.setOnTouchListenerToHideKeyboard(binding.root, requireActivity())
        (activity as HomeActivityUtility).hideBottomNavigationView()

        return binding.root
    }
}