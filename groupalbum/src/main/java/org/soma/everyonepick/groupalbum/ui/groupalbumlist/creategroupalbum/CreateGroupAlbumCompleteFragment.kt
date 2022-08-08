package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentCreateGroupAlbumCompleteBinding

class CreateGroupAlbumCompleteFragment : Fragment() {
    private var _binding: FragmentCreateGroupAlbumCompleteBinding? = null
    private val binding get() = _binding!!

    private val args: CreateGroupAlbumCompleteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateGroupAlbumCompleteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            title = args.title
            onClickConfirmButton = View.OnClickListener {
                val directions = CreateGroupAlbumCompleteFragmentDirections.toViewPagerFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}