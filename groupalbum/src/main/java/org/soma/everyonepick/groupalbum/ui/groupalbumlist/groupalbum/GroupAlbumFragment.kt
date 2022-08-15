package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.common.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumBinding
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.util.SelectionMode
import javax.inject.Inject

@AndroidEntryPoint
class GroupAlbumFragment: Fragment(), GroupAlbumFragmentListener {
    @Inject lateinit var groupAlbumUseCase: GroupAlbumUseCase
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase
    @Inject lateinit var userUseCase: UserUseCase

    private var _binding: FragmentGroupAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewModel by viewModels()
    private val args: GroupAlbumFragmentArgs by navArgs()

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

        lifecycleScope.launch {
            dataStoreUseCase.bearerAccessToken.first()?.let {
                viewModel.me = userUseCase.readUser(it)
                viewModel.groupAlbum.value = groupAlbumUseCase.readGroupAlbum(it, args.groupAlbumId)
            }
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.photoSelectionMode.observe(viewLifecycleOwner) { photoSelectionMode ->
            // 선택 모드일 때는 TabLayout을 비활성화 합니다.
            setTabLayoutEnabled(
                enabled = photoSelectionMode == SelectionMode.NORMAL_MODE.ordinal,
                binding.viewpager2,
                binding.tablayout
            )
        }

        viewModel.memberSelectionMode.observe(viewLifecycleOwner) { memberSelectionMode ->
            viewModel.setIsCheckboxVisible(memberSelectionMode == SelectionMode.SELECTION_MODE.ordinal)
        }

        viewModel.groupAlbum.observe(viewLifecycleOwner) {
            viewModel.updateMemberModelList()
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
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onResume() {
        super.onResume()
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
        viewModel.photoSelectionMode.value =
            if (viewModel.photoSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal) SelectionMode.SELECTION_MODE.ordinal
            else SelectionMode.NORMAL_MODE.ordinal
    }

    override fun onClickDrawerButton() {
        binding.drawerlayout.openDrawer(GravityCompat.END)
    }

    override fun onClickUpdateTitleButton() {
        UpdateTitleDialogFragment { newTitle ->
            lifecycleScope.launch {
                try {
                    val token = dataStoreUseCase.bearerAccessToken.first()!!
                    val groupAlbum = viewModel.groupAlbum.value!!.toGroupAlbum().apply {
                        title = newTitle
                    }
                    groupAlbumUseCase.updateGroupAlbum(token, viewModel.groupAlbum.value!!.id, groupAlbum)
                    viewModel.updateGroupAlbumTitle(newTitle)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "단체공유앨범 이름 변경에 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }.show(requireActivity().supportFragmentManager, "UpdateTitleDialogFragment")
    }

    override fun onClickExitButton() {
        AlertDialog.Builder(context).setMessage("단체공유앨범에서 나갑니다.")
            .setPositiveButton("나가기") { _, _ ->
                lifecycleScope.launch {
                    try {
                        val token = dataStoreUseCase.bearerAccessToken.first()!!
                        groupAlbumUseCase.leaveGroupAlbum(token, viewModel.groupAlbum.value!!.id)

                        findNavController().navigateUp()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "단체공유앨범에서 나가는 데 실패했습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
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


    companion object {
        private val TAB_ITEMS = listOf("사진", "합성중", "합성완료")
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