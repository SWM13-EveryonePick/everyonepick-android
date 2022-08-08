package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.pref.PreferencesDataStore
import org.soma.everyonepick.common.data.repository.UserRepository
import org.soma.everyonepick.common.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.groupalbum.adapter.MemberAdapter
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumViewPagerBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.GroupAlbumListFragment.Companion.GROUP_ALBUM_REMOVED
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel
import javax.inject.Inject

private val TAB_ITEMS = listOf("사진", "합성중", "합성완료")

@AndroidEntryPoint
class GroupAlbumViewPagerFragment: Fragment(), GroupAlbumViewPagerFragmentListener {
    @Inject lateinit var groupAlbumRepository: GroupAlbumRepository
    @Inject lateinit var preferencesDataStore: PreferencesDataStore
    @Inject lateinit var userRepository: UserRepository

    private var _binding: FragmentGroupAlbumViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewPagerViewModel by viewModels()
    private val args: GroupAlbumViewPagerFragmentArgs by navArgs()

    // 선택 모드일 때 뒤로가기 버튼을 누르면 선택 모드를 취소해야 하며, 이를 위한 콜백입니다.
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerlayout.closeDrawer(GravityCompat.END)
                } else if (viewModel.photoSelectionMode.value == SelectionMode.SELECTION_MODE.ordinal) {
                    viewModel.photoSelectionMode.value = SelectionMode.NORMAL_MODE.ordinal
                } else {
                    findNavController().navigateUp()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupAlbumViewPagerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = MemberAdapter(viewModel)
            it.viewModel = viewModel
            it.listener = this
        }

        lifecycleScope.launch {
            viewModel.groupAlbum.value = groupAlbumRepository.getGroupAlbum(args.groupAlbumId)
            viewModel.me = preferencesDataStore.accessToken.first()?.let {
                userRepository.getUser(it).data
            }
            viewModel.fetchMemberList()
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.photoSelectionMode.observe(viewLifecycleOwner) { photoSelectionMode ->
            setTabLayoutEnabled(
                enabled = photoSelectionMode == SelectionMode.NORMAL_MODE.ordinal,
                binding.viewpager2,
                binding.tablayout
            )
        }

        viewModel.memberSelectionMode.observe(viewLifecycleOwner) { memberSelectionMode ->
            viewModel.setIsCheckboxVisible(memberSelectionMode == SelectionMode.SELECTION_MODE.ordinal)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this)
            it.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.currentItem.value = it.currentItem
                }
            })
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = TAB_ITEMS[position]
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        (activity as org.soma.everyonepick.foundation.util.HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }


    override fun onClickSelectButton() {
        viewModel.photoSelectionMode.value =
            if (viewModel.photoSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal) SelectionMode.SELECTION_MODE.ordinal
            else SelectionMode.NORMAL_MODE.ordinal
    }

    override fun onClickDrawerButton() {
        binding.drawerlayout.openDrawer(GravityCompat.END)
    }

    override fun onClickUpdateDrawerTitleButton() {
        UpdateTitleDialogFragment {
            // TODO: API
            viewModel.updateGroupAlbumTitle(it)
        }.show(requireActivity().supportFragmentManager, "UpdateTitleDialogFragment")
    }

    override fun onClickDrawerExitButton() {
        AlertDialog.Builder(context).setMessage("단체공유앨범에서 나갑니다.")
            .setPositiveButton("확인") { _, _ ->
                // TODO: API
                activity?.supportFragmentManager?.setFragmentResult(
                    GROUP_ALBUM_REMOVED,
                    bundleOf("id" to args.groupAlbumId)
                )
                findNavController().navigateUp()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    override fun onClickKickIcon() {
        viewModel.memberSelectionMode.value = SelectionMode.SELECTION_MODE.ordinal
    }

    override fun onClickKickButton() {
        if (viewModel.checked.value == null || viewModel.checked.value == 0) return

        // TODO: API
        viewModel.removeCheckedItems()
        viewModel.memberSelectionMode.value = SelectionMode.NORMAL_MODE.ordinal
    }

    override fun onClickCancelKickButton() {
        viewModel.memberSelectionMode.value = SelectionMode.NORMAL_MODE.ordinal
    }
}

interface GroupAlbumViewPagerFragmentListener {
    fun onClickSelectButton()
    fun onClickDrawerButton()
    fun onClickUpdateDrawerTitleButton()
    fun onClickDrawerExitButton()
    fun onClickKickIcon()
    fun onClickKickButton()
    fun onClickCancelKickButton()
}