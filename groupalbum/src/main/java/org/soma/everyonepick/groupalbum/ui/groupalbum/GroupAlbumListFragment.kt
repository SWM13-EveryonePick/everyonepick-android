package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumListAdapter
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.data.GroupAlbumListItem
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumlistBinding
import org.soma.everyonepick.groupalbum.utility.GroupAlbumListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel

@AndroidEntryPoint
class GroupAlbumListFragment : Fragment() {
    private var _binding: FragmentGroupalbumlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumlistBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel
        binding.parentViewModel = parentViewModel

        val adapter = GroupAlbumListAdapter(viewModel)
        binding.recyclerviewGroupalbum.adapter = adapter

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: GroupAlbumListAdapter) {
        viewModel.groupAlbumListItemList.observe(viewLifecycleOwner) { groupAlbumListItems ->
            // toMutableList(): 참조 주소를 새롭게 함으로써 갱신이 되도록 한다.
            adapter.submitList(groupAlbumListItems.toMutableList())
        }

        parentViewModel.groupAlbumListMode.observe(viewLifecycleOwner) { groupAlbumListMode ->
            when(groupAlbumListMode) {
                GroupAlbumListMode.NORMAL_MODE.ordinal -> viewModel.setCheckboxGone()
                else -> viewModel.setCheckboxVisible()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    // TODO: Remove it after creating group album logic implemented
    fun onClickCreateGroupAlbumButton() {
        val index = viewModel.groupAlbumListItemList.value?.size?.toLong()
        viewModel.addGroupAlbum(GroupAlbumListItem(GroupAlbum(index ?: -1, "title$index"), false, false))
    }

    fun onClickDeleteButton() {
        viewModel.deleteCheckedItems()
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }

    fun onClickCancelButton() {
        parentViewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
    }
}