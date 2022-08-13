package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentInviteFriendBinding

@AndroidEntryPoint
class InviteFriendFragment : Fragment() {
    private var _binding: FragmentInviteFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InviteFriendViewModel by viewModels()

    // 친구가 한 명이라도 선택되어 있다면 정말 취소할지 물어봐야 하며, 이를 위한 콜백입니다.
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteFriendBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.adapter = InviteFriendAdapter(viewModel)
            it.viewModel = viewModel
            it.onClickNextButtonListener = View.OnClickListener {
                if (viewModel.checked.value!! <= 9) {
                    val directions = InviteFriendFragmentDirections.toGroupAlbumTitleFragment(viewModel.getCheckedFriendList().toTypedArray())
                    findNavController().navigate(directions)
                } else {
                    Toast.makeText(context, "선택 인원을 초과했어요! 초대 인원은 최대 9명까지입니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        (activity as HomeActivityUtil).hideBottomNavigationView()
        KeyboardUtil.setOnTouchListenerToHideKeyboard(binding.root, requireActivity(), listOf(binding.customactionbar, binding.buttonNext))
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.checked.value == 0) findNavController().navigateUp()
                else {
                    AlertDialog.Builder(context).setMessage("단체공유앨범 생성을 그만둡니다.")
                        .setPositiveButton("그만두기") { _, _ -> findNavController().navigateUp() }
                        .setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
                        .create().show()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}