package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumlistBinding
import org.soma.everyonepick.groupalbum.utility.GroupAlbumListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumParentViewPagerViewModel

@AndroidEntryPoint
class GroupAlbumListFragment : Fragment() {
    private var _binding: FragmentGroupalbumlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumListViewModel by viewModels()
    private val parentViewModel: GroupAlbumParentViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupalbumlistBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.adapter = GroupAlbumAdapter(viewModel)
        binding.viewModel = viewModel
        binding.parentViewModel = parentViewModel

        subscribeUi()
        setFragmentResultListeners()

        return binding.root
    }

    private fun subscribeUi() {
        parentViewModel.groupAlbumListMode.observe(viewLifecycleOwner) { groupAlbumListMode ->
            val isSelectionMode = groupAlbumListMode == GroupAlbumListMode.SELECTION_MODE.ordinal
            viewModel.setIsCheckboxVisible(isSelectionMode)

            if(isSelectionMode) (activity as HomeActivityUtility).hideBottomNavigationView()
            else (activity as HomeActivityUtility).showBottomNavigationView()
        }

        parentViewModel.checkAllTrigger.observe(viewLifecycleOwner) {
            viewModel.checkAll()
        }
    }

    // 내부 뎁스에서의 변경 사항을 받아와서 API call 없이 바로 적용합니다.
    // TODO: onResume()에서 자동 업데이트 -> 불필요한 로직이 되기 때문에 제거할 것
    private fun setFragmentResultListeners() {
        activity?.supportFragmentManager?.setFragmentResultListener(GROUP_ALBUM_REMOVED, viewLifecycleOwner) { _, bundle ->
            val id = bundle.getLong("id")
            viewModel.deleteGroupAlbum(id)
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: viewModel.fetchGroupAlbumItemList() -> 자동 업데이트
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** Databinding functions */
    fun onClickDeleteButton() {
        viewModel.deleteCheckedItems()
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }

    fun onClickCancelButton() {
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }

    companion object {
        // Request keys for FragmentResultListener
        const val GROUP_ALBUM_REMOVED = "group_album_removed"
    }
}