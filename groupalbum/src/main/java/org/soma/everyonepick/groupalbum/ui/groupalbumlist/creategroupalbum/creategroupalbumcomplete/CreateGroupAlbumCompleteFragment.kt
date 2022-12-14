package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.creategroupalbumcomplete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.common_ui.util.AnimationUtil
import org.soma.everyonepick.common.util.SHOWING_UP_ANIMATION_Y_OFFSET
import org.soma.everyonepick.groupalbum.databinding.FragmentCreateGroupAlbumCompleteBinding

class CreateGroupAlbumCompleteFragment : Fragment() {
    private var _binding: FragmentCreateGroupAlbumCompleteBinding? = null
    private val binding get() = _binding!!

    private val args: CreateGroupAlbumCompleteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroupAlbumCompleteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            title = args.title
            onClickConfirmButton = View.OnClickListener {
                val directions = CreateGroupAlbumCompleteFragmentDirections.toHomeViewPagerFragment()
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        AnimationUtil.startShowingUpAnimation(binding.root, SHOWING_UP_ANIMATION_Y_OFFSET, 700L)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}