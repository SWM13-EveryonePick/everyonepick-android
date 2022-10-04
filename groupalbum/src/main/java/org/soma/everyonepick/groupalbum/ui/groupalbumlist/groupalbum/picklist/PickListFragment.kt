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
            it.lifecycleOwner = this
            it.uncompletedAdapter = PickAdapter(this)
            it.completedAdapter = PickAdapter(this)
            it.viewModel = viewModel
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    parentViewModel.groupAlbum.collect {
                        if (it.id != null && it.id != GroupAlbum.dummyData.id)
                            viewModel.readPickModelList(it.id)
                    }
                }

                launch {
                    viewModel.toastMessage.collectLatest {
                        if (it.isNotEmpty()) {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** [PickAdapterCallback] */
    override fun navigateToPickFragment(pickId: Long) {
        val groupAlbumId = parentViewModel.groupAlbum.value.id?: -1
        viewModel.readPickDetail(groupAlbumId, pickId) { pickDetail ->
            val directions = GroupAlbumFragmentDirections.toPickFragment(
                groupAlbumId,
                pickDetail.photos.map { it.id }.toLongArray(),
                pickDetail.photos.map { it.photoUrl }.toTypedArray(),
                PickFragmentType.TO_SELECT
            )
            findNavController().navigate(directions)
        }
    }

    override fun navigateToPickStatusFragment(pickId: Long) {
        val groupAlbumId = parentViewModel.groupAlbum.value.id?: -1
        viewModel.readPickDetail(groupAlbumId, pickId) { pickDetail ->
            // TODO: navigate to PickStatusFragment
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = PickListFragment()
    }
}