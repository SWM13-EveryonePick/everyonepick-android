package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import android.widget.Toast
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
                if (viewModel.checked.value!! <= 9) {
                    val directions = InviteFriendFragmentDirections.toGroupAlbumTitleFragment(
                        viewModel.getCheckedFriendList().toTypedArray()
                    )
                    findNavController().navigate(directions)
                } else {
                    Toast.makeText(context, "선택 인원을 초과했어요! 초대 인원은 최대 9명까지입니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        KeyboardUtil.setOnTouchListenerToHideKeyboard(binding.root, requireActivity(), listOf(binding.customactionbar, binding.buttonNext))
        (activity as HomeActivityUtility).hideBottomNavigationView()

        return binding.root
    }
}