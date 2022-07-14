package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumBinding

class GroupAlbumFragment : Fragment() {
    private var _binding: FragmentGroupalbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumBinding.inflate(inflater, container, false)
        initializeRecyclerView()

        return binding.root
    }

    private fun initializeRecyclerView() {
        val adapter = GroupAlbumAdapter()
        binding.recyclerviewGroupalbum.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}