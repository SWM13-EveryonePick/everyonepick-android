package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewModel

@AndroidEntryPoint
class GroupAlbumFragment : Fragment() {
    private var _binding: FragmentGroupalbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initializeRecyclerView()

        return binding.root
    }

    private fun initializeRecyclerView() {
        val adapter = GroupAlbumAdapter()
        binding.recyclerviewGroupalbum.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: GroupAlbumAdapter) {
        viewModel.groupAlbumList.observe(viewLifecycleOwner) { groupAlbumList ->
            adapter.submitList(groupAlbumList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}