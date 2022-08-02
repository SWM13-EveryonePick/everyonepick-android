package org.soma.everyonepick.groupalbum.ui.friendlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.FriendAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentFriendListBinding
import org.soma.everyonepick.groupalbum.viewmodel.FriendListViewModel

@AndroidEntryPoint
class FriendListFragment : Fragment() {
    private var _binding: FragmentFriendListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = FriendAdapter()
            it.viewModel = viewModel
            it.onClickKakaoButton = View.OnClickListener {
                // TODO: 카카오톡 메시지 보내기
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.friends.value = viewModel.friends.value
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}