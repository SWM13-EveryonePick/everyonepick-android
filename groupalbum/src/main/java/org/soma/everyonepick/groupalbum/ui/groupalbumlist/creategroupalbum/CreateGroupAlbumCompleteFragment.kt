package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentCreateGroupAlbumCompleteBinding

class CreateGroupAlbumCompleteFragment : Fragment() {
    private var _binding: FragmentCreateGroupAlbumCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateGroupAlbumCompleteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onClickConfirmButton = View.OnClickListener {
                val directions = CreateGroupAlbumCompleteFragmentDirections.toViewPagerFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }
}