package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumBinding
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragment
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@AndroidEntryPoint
class GroupAlbumFragment: Fragment(), GroupAlbumFragmentListener {
    @Inject lateinit var groupAlbumUseCase: GroupAlbumUseCase
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
                inviteUsersToGroupAlbum(friendList)
            }
        }
    }

    private fun inviteUsersToGroupAlbum(friendList: MutableList<Friend>) {
        lifecycleScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val data = groupAlbumUseCase
                    .inviteUsersToGroupAlbum(token, viewModel.groupAlbum.value.id!!, friendList)

                viewModel.setGroupAlbum(data)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "단체공유앨범 초대에 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this)
            it.setOnPageSelectedListener { position -> viewModel.setViewPagerPosition(position) }
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = TAB_ITEMS[position]
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


    /** GroupAlbumFragmentListener */
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
            lifecycleScope.launch {
                try {
                    val token = dataStoreUseCase.bearerAccessToken.first()!!
                    val groupAlbum = viewModel.groupAlbum.value.copy(title = newTitle)

                    val data = groupAlbumUseCase.updateGroupAlbum(token, viewModel.groupAlbum.value.id!!, groupAlbum)
                    viewModel.setGroupAlbum(data)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "단체공유앨범 이름 변경에 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }.show(requireActivity().supportFragmentManager, "UpdateTitleDialogFragment")
    }

    override fun onClickExitButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage("단체공유앨범에서 나갑니다.")
            .setPositiveButtonText("나가기")
            .setOnClickPositiveButton {
                lifecycleScope.launch {
                    try {
                        val token = dataStoreUseCase.bearerAccessToken.first()!!
                        groupAlbumUseCase.leaveGroupAlbum(token, viewModel.groupAlbum.value.id!!)

                        findNavController().navigateUp()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "단체공유앨범에서 나가는 데 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .build().show()
    }

    override fun onClickKickIcon() {
        viewModel.setMemberSelectionMode(SelectionMode.SELECTION_MODE)
    }

    override fun onClickKickButton() {
        if (viewModel.checked.value == 0) return

        lifecycleScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val groupAlbumId = viewModel.groupAlbum.value.id
                val userListToKick = viewModel.getCheckedUserList()

                val data = groupAlbumUseCase.kickUsersOutOfGroupAlbum(token, groupAlbumId!!, userListToKick)
                viewModel.setGroupAlbum(data)

                viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "사용자를 강퇴하는 데 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickCancelKickButton() {
        viewModel.setMemberSelectionMode(SelectionMode.NORMAL_MODE)
    }


    companion object {
        private val TAB_ITEMS = listOf("사진", "합성중", "합성완료")
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