package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.talk.model.Friend
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragment
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@AndroidEntryPoint
class GroupAlbumFragment: Fragment(), GroupAlbumFragmentListener {
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

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

        showSyntheticTutorialAtFirst()

        subscribeUi()
        setFragmentResultListener()

        return binding.root
    }

    private fun showSyntheticTutorialAtFirst() {
        lifecycleScope.launch {
            dataStoreUseCase.run {
                val hasSyntheticTutorialShown = hasSyntheticTutorialShown.first()
                if (hasSyntheticTutorialShown != true) {
                    binding.layoutTutorial.visibility = View.VISIBLE
                    editHasSyntheticTutorialShown(true)
                }
            }
        }
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
                    viewModel.resultPhotoSelectionMode.collectLatest { resultPhotoSelectionMode ->
                        // 선택 모드일 때는 TabLayout을 비활성화 합니다.
                        setTabLayoutEnabled(
                            enabled = resultPhotoSelectionMode == SelectionMode.NORMAL_MODE.ordinal,
                            binding.viewpager2,
                            binding.tablayout
                        )
                    }
                }

                launch {
                    viewModel.memberSelectionMode.collectLatest { memberSelectionMode ->
                        viewModel.setIsCheckboxVisibleOfMember(memberSelectionMode == SelectionMode.SELECTION_MODE.ordinal)
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
     * [InviteFriendFragment]에서 선택한 Friend 리스트를 받습니다. 또한 [UpdateTitleDialogFragment]에서 입력한
     * 새 단체공유앨범명을 받습니다.
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

        activity?.supportFragmentManager?.setFragmentResultListener(
            UPDATE_TITLE_DIALOG_REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(UPDATE_TITLE_DIALOG_KEY)?.let { newTitle ->
                viewModel.updateGroupAlbum(newTitle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this)
            it.setOnPageSelectedListener { position -> viewModel.setViewPagerPosition(position) }
        }
        val tabItems = listOf(getString(R.string.tab_text_photo), getString(R.string.tab_text_pick), getString(R.string.tab_text_complete))
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
        _binding = null
        super.onDestroy()
    }


    /** [GroupAlbumFragmentListener] */
    override fun onClickSelectButton() {
        when (viewModel.viewPagerPosition.value) {
            0 -> {
                viewModel.setPhotoSelectionMode(
                    if (viewModel.photoSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal) SelectionMode.SELECTION_MODE
                    else SelectionMode.NORMAL_MODE
                )
            }
            2 -> {
                viewModel.setResultPhotoSelectionMode(
                    if (viewModel.resultPhotoSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal) SelectionMode.SELECTION_MODE
                    else SelectionMode.NORMAL_MODE
                )
            }
        }
    }

    override fun onClickDrawerButton() {
        binding.drawerlayout.openDrawer(GravityCompat.END)
    }

    override fun onClickUpdateTitleButton() {
        UpdateTitleDialogFragment().show(requireActivity().supportFragmentManager, "UpdateTitleDialogFragment")
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
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_kick_checked_member))
            .setOnClickPositiveButton {
                viewModel.kickCheckedUsersOutOfGroupAlbum()
                viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
            }
            .build().show()
    }

    override fun onClickCancelKickButton() {
        viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onClickTutorialListener() {
        binding.layoutTutorial.visibility = View.GONE
    }


    companion object {
        const val FRIEND_LIST_TO_INVITE_REQUEST_KEY = "friend_list_to_invite_request_key"
        const val FRIEND_LIST_TO_INVITE_KEY = "friend_list_to_invite_key"
        const val UPDATE_TITLE_DIALOG_REQUEST_KEY = "update_title_dialog_request_key"
        const val UPDATE_TITLE_DIALOG_KEY = "update_title_dialog_key"
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
    fun onClickTutorialListener()
}