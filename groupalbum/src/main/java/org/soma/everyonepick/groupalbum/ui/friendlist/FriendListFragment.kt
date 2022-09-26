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
                // TODO: 스토어 링크 변경
                val textTemplate = TextTemplate(
                    text = getString(R.string.kakao_message_text).trimIndent(),
                    link = Link(
                        webUrl = "https://play.google.com/",
                        mobileWebUrl = "https://play.google.com/"
                    )
                )
                shareKakaoMessage(textTemplate)
            }
        }

        return binding.root
    }

    private fun shareKakaoMessage(template: DefaultTemplate) {
        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(requireContext(), template) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
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
        super.onDestroy()
        _binding = null
    }


    companion object {
        private const val TAG = "FriendListFragment"
    }
}