package org.soma.everyonepick.groupalbum.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumviewpagerBinding

class GroupAlbumViewPagerFragment : Fragment() {
    private var _binding: FragmentGroupalbumviewpagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumviewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewpager2.adapter = GroupAlbumAdapter(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}