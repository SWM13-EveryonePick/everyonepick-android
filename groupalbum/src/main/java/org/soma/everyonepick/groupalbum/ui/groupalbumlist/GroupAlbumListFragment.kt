package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumListBinding
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragmentType
import javax.inject.Inject

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
            it.adapter = GroupAlbumAdapter(parentViewModel)
            it.viewModel = viewModel
            it.parentViewModel = parentViewModel
            it.listener = this
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        binding.swiperefreshlayout.setOnRefreshListener {
            viewModel.readGroupAlbumModelList {
                binding.swiperefreshlayout.isRefreshing = false
            }
        }

        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    parentViewModel.selectionMode.collectLatest { selectionMode ->
                        val isSelectionMode = selectionMode == SelectionMode.SELECTION_MODE.ordinal
                        viewModel.setIsCheckboxVisible(isSelectionMode)

                        if (isSelectionMode) (activity as HomeActivityUtil).hideBottomNavigationView()
                        else (activity as HomeActivityUtil).showBottomNavigationView()
                    }
                }

                launch {
                    parentViewModel.checkAllTrigger.collectLatest {
                        viewModel.checkAll()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        parentViewModel.setSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onResume() {
        super.onResume()
        viewModel.readGroupAlbumModelList()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** GroupAlbumListFragmentListener */
    override fun onClickDeleteButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_exit_group_album))
            .setOnClickPositiveButton {
                viewModel.leaveCheckedGroupAlbum()
                parentViewModel.setSelectionMode(SelectionMode.NORMAL_MODE)
            }
            .build().show()
    }

    override fun onClickCancelButton() {
        parentViewModel.setSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onClickCreateGroupAlbumButton() {
        val directions = HomeViewPagerFragmentDirections.toInviteFriendFragment(InviteFriendFragmentType.TO_CREATE)
        findNavController().navigate(directions)
    }

    companion object {
        @JvmStatic
        fun newInstance() = GroupAlbumListFragment()
    }
}

interface GroupAlbumListFragmentListener {
    fun onClickDeleteButton()
    fun onClickCancelButton()
    fun onClickCreateGroupAlbumButton()
}