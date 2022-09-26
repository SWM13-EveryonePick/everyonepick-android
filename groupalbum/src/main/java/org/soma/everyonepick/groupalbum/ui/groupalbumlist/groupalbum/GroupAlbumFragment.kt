package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.talk.model.Friend
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragment
import org.soma.everyonepick.groupalbum.util.SelectionMode

@AndroidEntryPoint
class GroupAlbumFragment: Fragment(), GroupAlbumFragmentListener {
    private var _binding: FragmentGroupAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewModel by viewModels()

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupAlbumBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = MemberAdapter(viewModel)
            it.viewModel = viewModel
            it.listener = this
        }

        subscribeUi()
        setFragmentResultListener()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.photoSelectionMode.collectLatest { photoSelectionMode ->
                        // 선택 모드일 때는 TabLayout을 비활성화 합니다.
                        setTabLayoutEnabled(
                            enabled = photoSelectionMode == SelectionMode.NORMAL_MODE.ordinal,
                            binding.viewpager2,
                            binding.tablayout
                        )
                    }
                }

                launch {
                    viewModel.memberSelectionMode.collectLatest { memberSelectionMode ->
                        viewModel.setIsCheckboxVisible(memberSelectionMode == SelectionMode.SELECTION_MODE.ordinal)
                    }
                }

                launch {
                    viewModel.toastMessage.collectLatest {
                        if (it.isNotEmpty()) {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    /**
     * [InviteFriendFragment]에서 선택한 Friend 리스트를 받습니다.
     */
    private fun setFragmentResultListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            FRIEND_LIST_TO_INVITE_REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            bundle.get(FRIEND_LIST_TO_INVITE_KEY)?.let { friendList ->
                friendList as MutableList<Friend>
                viewModel.inviteUsersToGroupAlbum(friendList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this)
            it.setOnPageSelectedListener { position -> viewModel.setViewPagerPosition(position) }
        }
        val tabItems = listOf(getString(R.string.tab_text_photo), getString(R.string.tab_text_in_progress), getString(R.string.tab_text_complete))
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = tabItems[position]
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Drawer가 열려있는 경우
                if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerlayout.closeDrawer(GravityCompat.END)
                }
                // 선택 모드인 경우
                else if (viewModel.photoSelectionMode.value == SelectionMode.SELECTION_MODE.ordinal) {
                    viewModel.setPhotoSelectionMode(SelectionMode.NORMAL_MODE)
                } else {
                    findNavController().navigateUp()
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
        super.onDestroy()
        _binding = null
    }


    /** [GroupAlbumFragmentListener] */
    override fun onClickSelectButton() {
        viewModel.setPhotoSelectionMode(
            if (viewModel.photoSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal) SelectionMode.SELECTION_MODE
            else SelectionMode.NORMAL_MODE
        )
    }

    override fun onClickDrawerButton() {
        binding.drawerlayout.openDrawer(GravityCompat.END)
    }

    override fun onClickUpdateTitleButton() {
        UpdateTitleDialogFragment { newTitle ->
           viewModel.updateGroupAlbum(newTitle)
        }.show(requireActivity().supportFragmentManager, "UpdateTitleDialogFragment")
    }

    override fun onClickExitButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_exit_group_album))
            .setPositiveButtonText(getString(R.string.exit))
            .setOnClickPositiveButton {
                viewModel.leaveGroupAlbum()
                findNavController().navigateUp()
            }
            .build().show()
    }

    override fun onClickKickIcon() {
        viewModel.setMemberSelectionMode(SelectionMode.SELECTION_MODE)
    }

    override fun onClickKickButton() {
        if (viewModel.checked.value == 0) return
        viewModel.kickUsersOutOfGroupAlbum()
        viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onClickCancelKickButton() {
        viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
    }


    companion object {
        const val FRIEND_LIST_TO_INVITE_REQUEST_KEY = "friend_list_to_invite_request_key"
        const val FRIEND_LIST_TO_INVITE_KEY = "friend_list_to_invite_key"
    }
}

interface GroupAlbumFragmentListener {
    fun onClickSelectButton()
    fun onClickDrawerButton()
    fun onClickUpdateTitleButton()
    fun onClickExitButton()
    fun onClickKickIcon()
    fun onClickKickButton()
    fun onClickCancelKickButton()
}