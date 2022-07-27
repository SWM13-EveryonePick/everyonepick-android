package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.soma.everyonepick.groupalbum.R
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
                /* TODO: API 호출 + 성공할 때까지 대기 후 나가기
                args.inviteFriends
                viewModel.title
                */
                val directions = GroupAlbumTitleFragmentDirections.toViewPagerFragment()
                findNavController().navigate(directions)
                Toast.makeText(context, "단체공유앨범을 생성했습니다!", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
}