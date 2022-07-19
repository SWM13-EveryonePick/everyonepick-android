package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumviewpagerBinding

class GroupAlbumViewPagerFragment : Fragment() {
    private var _binding: FragmentGroupalbumviewpagerBinding? = null
    private val binding get() = _binding!!

    private val args: GroupAlbumViewPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumviewpagerBinding.inflate(inflater, container, false)

        return binding.root
    }
}