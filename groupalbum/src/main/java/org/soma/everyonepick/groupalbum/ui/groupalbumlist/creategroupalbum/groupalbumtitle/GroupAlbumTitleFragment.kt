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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.KeyboardUtil
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupAlbumTitleBinding
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@AndroidEntryPoint
class GroupAlbumTitleFragment : Fragment() {
    private var _binding: FragmentGroupAlbumTitleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumTitleViewModel by viewModels()

    private val args: GroupAlbumTitleFragmentArgs by navArgs()

    @Inject lateinit var groupAlbumUseCase: GroupAlbumUseCase
    @Inject lateinit var dataStoreUseCase: DataStoreUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupAlbumTitleBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.onClickCreateButton = View.OnClickListener {
                lifecycleScope.launch {
                    try {
                        val token = dataStoreUseCase.bearerAccessToken.first()!!
                        val groupAlbum = GroupAlbum(
                            viewModel.title.value!!,
                            getUserListToCreateGroupAlbum()
                        )
                        groupAlbumUseCase.createGroupAlbum(token, groupAlbum)

                        KeyboardUtil.hideKeyboard(requireActivity())

                        val directions = GroupAlbumTitleFragmentDirections.toCreateGroupAlbumCompleteFragment(viewModel.title.value?: "")
                        findNavController().navigate(directions)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "단체공유앨범 생성에 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    /**
     * 서버 설계상, 단체공유앨범 생성을 위한 유저 리스트에서는 [User.clientId] 내용만 포함되면 됩니다.
     */
    private fun getUserListToCreateGroupAlbum(): List<User> {
        val userList = mutableListOf<User>()
        args.inviteFriends.forEach {
            userList.add(User(null, null, it.id.toString(), null, null))
        }
        return userList
    }

    override fun onStart() {
        super.onStart()

        (activity as HomeActivityUtil).hideBottomNavigationView()
        KeyboardUtil.showKeyboard(binding.edittextTitle, requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}