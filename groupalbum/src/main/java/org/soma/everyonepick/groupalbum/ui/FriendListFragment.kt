package org.soma.everyonepick.groupalbum.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.soma.everyonepick.groupalbum.adapter.FriendAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentFriendlistBinding
import org.soma.everyonepick.groupalbum.viewmodel.FriendListViewModel

class FriendListFragment : Fragment() {
    private var _binding: FragmentFriendlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendlistBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.adapter = FriendAdapter()
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}