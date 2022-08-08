package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.groupalbum.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumListBinding
import org.soma.everyonepick.groupalbum.ui.ViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel
import org.soma.everyonepick.groupalbum.viewmodel.ViewPagerViewModel

@AndroidEntryPoint
class GroupAlbumListFragment : Fragment(), GroupAlbumListFragmentListener {
    private var _binding: FragmentGroupAlbumListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumListViewModel by viewModels()
    private val parentViewModel: ViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupAlbumListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = GroupAlbumAdapter(viewModel)
            it.viewModel = viewModel
            it.parentViewModel = parentViewModel
            it.listener = this
        }

        subscribeUi()
        setFragmentResultListeners()

        return binding.root
    }

    private fun subscribeUi() {
        parentViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            val isSelectionMode = selectionMode == SelectionMode.SELECTION_MODE.ordinal
            viewModel.setIsCheckboxVisible(isSelectionMode)

            if (isSelectionMode) (activity as org.soma.everyonepick.foundation.util.HomeActivityUtil).hideBottomNavigationView()
            else (activity as org.soma.everyonepick.foundation.util.HomeActivityUtil).showBottomNavigationView()
        }

        parentViewModel.checkAllTrigger.observe(viewLifecycleOwner) {
            viewModel.checkAll()
        }
    }

    // 내부 뎁스에서의 변경 사항을 받아와서 API call 없이 바로 적용합니다.
    // TODO: onResume() 또는 onStart()에서 자동 업데이트 -> 불필요한 로직이 되기 때문에 제거할 것
    private fun setFragmentResultListeners() {
        activity?.supportFragmentManager?.setFragmentResultListener(GROUP_ALBUM_REMOVED, viewLifecycleOwner) { _, bundle ->
            val id = bundle.getLong("id")
            viewModel.deleteGroupAlbum(id)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchGroupAlbumItemList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** GroupAlbumListFragmentListener */
    override fun onClickDeleteButton() {
        viewModel.deleteCheckedItems()
        parentViewModel.selectionMode.value = SelectionMode.NORMAL_MODE.ordinal
    }

    override fun onClickCancelButton() {
        parentViewModel.selectionMode.value = SelectionMode.NORMAL_MODE.ordinal
    }

    override fun onClickCreateGroupAlbumButton() {
        val directions = ViewPagerFragmentDirections.toInvitationFragment()
        findNavController().navigate(directions)
    }


    companion object {
        // Request keys for FragmentResultListener
        const val GROUP_ALBUM_REMOVED = "group_album_removed"
    }
}

interface GroupAlbumListFragmentListener {
    fun onClickDeleteButton()
    fun onClickCancelButton()
    fun onClickCreateGroupAlbumButton()
}