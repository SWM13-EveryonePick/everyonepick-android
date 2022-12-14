package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.picklist

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.FragmentPickListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick.PickFragmentType

@AndroidEntryPoint
class PickListFragment : Fragment(), PickAdapterCallback {
    private var _binding: FragmentPickListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PickListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewModel by viewModels(ownerProducer = { requireParentFragment() } )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.uncompletedAdapter = PickAdapter(this)
            it.completedAdapter = PickAdapter(this)
            it.viewModel = viewModel
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        binding.swiperefreshlayout.setOnRefreshListener {
            viewModel.readPickModelList(parentViewModel.groupAlbum.value.id) {
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    parentViewModel.groupAlbum.collect {
                        viewModel.readPickModelList(it.id)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.readPickModelList(parentViewModel.groupAlbum.value.id)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** [PickAdapterCallback] */
    override fun navigateToPickFragment(pickId: Long) {
        val groupAlbumId = parentViewModel.groupAlbum.value.id?: -1
        viewModel.readPickDetail(groupAlbumId, pickId) { pickDetail ->
            val directions = GroupAlbumFragmentDirections.toPickFragment(
                groupAlbumId,
                pickDetail.photos.map { it.id }.toLongArray(),
                pickDetail.photos.map { it.photoUrl }.toTypedArray(),
                PickFragmentType.TO_SELECT,
                pickId
            )
            findNavController().navigate(directions)
        }
    }

    override fun navigateToPickInfoFragment(pickId: Long) {
        viewModel.readPickInfo(pickId) {
            val directions = GroupAlbumFragmentDirections.toPickInfoFragment(it.userCount, it.pickUserCount, it.timeout)
            findNavController().navigate(directions)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PickListFragment()
    }
}