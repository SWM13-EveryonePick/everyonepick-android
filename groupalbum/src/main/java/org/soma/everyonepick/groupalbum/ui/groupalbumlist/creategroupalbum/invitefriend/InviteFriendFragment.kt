package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.common_ui.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentInviteFriendBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragment
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragment.Companion.FRIEND_LIST_TO_INVITE_KEY
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragment.Companion.FRIEND_LIST_TO_INVITE_REQUEST_KEY

/**
 * 이 프래그먼트의 [navArgs]로 [InviteFriendFragmentType]가 전달됩니다.
 * [InviteFriendFragmentType.TO_CREATE]일 경우 이 프래그먼트는 단체공유앨범 생성을 위해 인원을 선택하는 페이지가 되고,
 * [InviteFriendFragmentType.TO_INVITE]일 경우에는, 기존 단체공유앨범에 인원을 추가로 초대하는 페이지가 됩니다.
 */
@AndroidEntryPoint
class InviteFriendFragment: Fragment() {
    private var _binding: FragmentInviteFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InviteFriendViewModel by viewModels()
    private val args: InviteFriendFragmentArgs by navArgs()

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteFriendBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = InviteFriendAdapter()
            it.viewModel = viewModel
            it.onClickNextButtonListener = View.OnClickListener {
                if (viewModel.checked.value <= viewModel.maxInviteCount.value) {
                    when (args.inviteFriendFragmentType) {
                        InviteFriendFragmentType.TO_CREATE -> navigateToGroupAlbumTitleFragment()
                        else -> returnToGroupAlbumFragment()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.toast_exceed_selection, viewModel.maxInviteCount.value), Toast.LENGTH_LONG).show()
                }
            }
        }

        return binding.root
    }

    private fun navigateToGroupAlbumTitleFragment() {
        val directions = InviteFriendFragmentDirections
            .toGroupAlbumTitleFragment(viewModel.getCheckedFriendList().toTypedArray())
        findNavController().navigate(directions)
    }

    /**
     * [GroupAlbumFragment]로 복귀합니다. 초대 API 호출을 [GroupAlbumFragment]에서 진행하므로, 이곳에서 체크한 친구
     * 목록 데이터를 전달합니다.
     */
    private fun returnToGroupAlbumFragment() {
        activity?.supportFragmentManager?.setFragmentResult(
            FRIEND_LIST_TO_INVITE_REQUEST_KEY,
            bundleOf(FRIEND_LIST_TO_INVITE_KEY to viewModel.getCheckedFriendList())
        )
        findNavController().navigateUp()
    }

    override fun onStart() {
        super.onStart()

        (activity as HomeActivityUtil).hideBottomNavigationView()
        KeyboardUtil.setOnTouchListenerToHideKeyboard(binding.root, requireActivity(), listOf(binding.customactionbar, binding.buttonNext))
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.checked.value == 0) findNavController().navigateUp()
                else {
                    DialogWithTwoButton.Builder(requireContext())
                        .setMessage(getString(R.string.dialog_give_up_to_invite))
                        .setPositiveButtonText(getString(R.string.give_up))
                        .setOnClickPositiveButton { findNavController().navigateUp() }
                        .build().show()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

enum class InviteFriendFragmentType { TO_CREATE, TO_INVITE }