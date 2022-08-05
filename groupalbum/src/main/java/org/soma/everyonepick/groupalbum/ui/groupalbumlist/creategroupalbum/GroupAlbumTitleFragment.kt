package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.common.utility.HomeActivityUtility
import org.soma.everyonepick.common_ui.KeyboardUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumTitleBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumTitleViewModel

class GroupAlbumTitleFragment : Fragment() {
    private var _binding: FragmentGroupAlbumTitleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumTitleViewModel by viewModels()

    private val args: GroupAlbumTitleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupAlbumTitleBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.onClickCreateButton = View.OnClickListener {
                /* TODO: API 호출 + 성공할 때까지 대기 후 내비게이션
                args.inviteFriends
                viewModel.title
                */
                KeyboardUtil.hideKeyboard(requireActivity())

                val directions = GroupAlbumTitleFragmentDirections.toCreateGroupAlbumCompleteFragment(
                    viewModel.title.value?: ""
                )
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        (activity as HomeActivityUtility).hideBottomNavigationView()
        KeyboardUtil.showKeyboard(binding.edittextTitle, requireActivity())
    }
}