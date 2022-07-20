package org.soma.everyonepick.groupalbum.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
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
    ): View? {
        _binding = FragmentGroupalbumlistBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel
        binding.parentViewModel = parentViewModel

        val adapter = GroupAlbumAdapter(viewModel)
        binding.recyclerviewGroupalbum.adapter = adapter

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: GroupAlbumAdapter) {
        viewModel.groupAlbumItemList.observe(viewLifecycleOwner) { groupAlbumItemList ->
            adapter.submitList(groupAlbumItemList.toMutableList())
        }

        parentViewModel.groupAlbumListMode.observe(viewLifecycleOwner) { groupAlbumListMode ->
            viewModel.setIsCheckboxVisible(groupAlbumListMode == GroupAlbumListMode.SELECTION_MODE.ordinal)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // TODO: Remove it after creating group album logic implemented
    fun onClickCreateGroupAlbumButton() {
        val index = viewModel.groupAlbumItemList.value?.size?.toLong()
        viewModel.addGroupAlbumItem(GroupAlbumItem(GroupAlbumDao(index ?: -1, "title$index"), false, false))
    }

    fun onClickDeleteButton() {
        viewModel.deleteCheckedItems()
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }

    fun onClickCancelButton() {
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }
}