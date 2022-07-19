package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumBinding

class GroupAlbumFragment : Fragment() {
    private var _binding: FragmentGroupalbumBinding? = null
    private val binding get() = _binding!!

    private val args: GroupAlbumFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text1.text = args.groupAlbumId.toString()
    }
}