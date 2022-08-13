package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumListBinding
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerViewModel

@AndroidEntryPoint
class GroupAlbumListFragment : Fragment(), GroupAlbumListFragmentListener {
    private var _binding: FragmentGroupAlbumListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumListViewModel by viewModels()
    private val parentViewModel: HomeViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })

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

        return binding.root
    }

    private fun subscribeUi() {
        parentViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            val isSelectionMode = selectionMode == SelectionMode.SELECTION_MODE.ordinal
            viewModel.setIsCheckboxVisible(isSelectionMode)

            if (isSelectionMode) (activity as HomeActivityUtil).hideBottomNavigationView()
            else (activity as HomeActivityUtil).showBottomNavigationView()
        }

        parentViewModel.checkAllTrigger.observe(viewLifecycleOwner) {
            viewModel.checkAll()
        }
    }

    override fun onStart() {
        super.onStart()

        parentViewModel.selectionMode.value = SelectionMode.NORMAL_MODE.ordinal
        viewModel.readGroupAlbumModelList()
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
        val directions = HomeViewPagerFragmentDirections.toInvitationFragment()
        findNavController().navigate(directions)
    }
}

interface GroupAlbumListFragmentListener {
    fun onClickDeleteButton()
    fun onClickCancelButton()
    fun onClickCreateGroupAlbumButton()
}