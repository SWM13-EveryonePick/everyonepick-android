package org.soma.everyonepick.groupalbum.ui.friend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kakao.sdk.talk.TalkApiClient
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.FriendAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentFriendBinding
import org.soma.everyonepick.groupalbum.viewmodel.FriendViewModel

@AndroidEntryPoint
class FriendFragment : Fragment() {
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)

        val adapter = FriendAdapter()
        binding.recyclerviewFriend.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: FriendAdapter) {
        viewModel.friends.observe(viewLifecycleOwner) { friends ->
            adapter.submitList(friends.elements)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "FriendFragment"
    }
}