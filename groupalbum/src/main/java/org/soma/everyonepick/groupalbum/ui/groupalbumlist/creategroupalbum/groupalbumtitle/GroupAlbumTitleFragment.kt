package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.groupalbumtitle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.util.KakaoUtil.Companion.toUserWithClientId
import org.soma.everyonepick.common_ui.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumTitleBinding

@AndroidEntryPoint
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
                viewModel.createGroupAlbum(getUserListToCreateGroupAlbum()) {
                    KeyboardUtil.hideKeyboard(requireActivity())
                    val directions = GroupAlbumTitleFragmentDirections.toCreateGroupAlbumCompleteFragment(viewModel.title.value)
                    findNavController().navigate(directions)
                }
            }
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getUserListToCreateGroupAlbum(): List<User> {
        // ?????? ?????????, ?????????????????? ????????? ?????? ?????? ?????????????????? [User.clientId] ????????? ???????????? ?????????.
        return args.inviteFriends.map { it.toUserWithClientId() }
    }

    override fun onStart() {
        super.onStart()

        (activity as HomeActivityUtil).hideBottomNavigationView()
        KeyboardUtil.showKeyboard(binding.edittextTitle, requireActivity())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}