package org.soma.everyonepick.groupalbum.ui.friendlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.DefaultTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentFriendListBinding

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
                val textTemplate = TextTemplate(
                    text = getString(R.string.kakao_message_text).trimIndent(),
                    link = Link(
                        webUrl = "https://play.google.com/store/apps/details?id=org.soma.everyonepick.app",
                        mobileWebUrl = "https://play.google.com/store/apps/details?id=org.soma.everyonepick.app"
                    )
                )
                shareKakaoMessage(textTemplate)
            }
        }

        return binding.root
    }

    private fun shareKakaoMessage(template: DefaultTemplate) {
        // ???????????? ???????????? ??????
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            // ?????????????????? ???????????? ?????? ??????
            ShareClient.instance.shareDefault(requireContext(), template) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "???????????? ?????? ??????", error)
                } else if (sharingResult != null) {
                    Log.d(TAG, "???????????? ?????? ?????? ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // ???????????? ????????? ??????????????? ?????? ?????? ???????????? ????????? ?????? ?????? ???????????? ?????? ???????????? ?????? ??? ????????????.
                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.toast_kakao_talk_is_not_installed), Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.readFriends()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    companion object {
        private const val TAG = "FriendListFragment"

        @JvmStatic
        fun newInstance() = FriendListFragment()
    }
}