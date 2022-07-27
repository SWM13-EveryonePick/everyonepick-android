package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.InviteFriendAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentInviteFriendBinding
import org.soma.everyonepick.groupalbum.viewmodel.InviteFriendViewModel

class InviteFriendFragment : Fragment() {
    private var _binding: FragmentInviteFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InviteFriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInviteFriendBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = InviteFriendAdapter(viewModel)
            it.viewModel = viewModel
            it.onClickNextButtonListener = View.OnClickListener {
                // TODO: 다음 페이지
            }
        }

        return binding.root
    }
}