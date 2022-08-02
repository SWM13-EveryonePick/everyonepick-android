package org.soma.everyonepick.groupalbum.ui.friendlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.DefaultTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
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
                // TODO: 앱 딥링크(or 스토어 링크)로 변경 ... 앱 미설치 상태에서 딥링크 들어가면 스토어로 이동되는지 의문
                val textTemplate = TextTemplate(
                    text = """
                        모두가 마음에 들어하는 사진을 쉽고 빠르게, 인공지능 단체사진 합성 서비스 [모두의 PICK]에 초대합니다!
                    """.trimIndent(),
                    link = Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
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
            Toast.makeText(requireContext(), "카카오톡이 설치되어 있지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.friends.value = viewModel.friends.value
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        private const val TAG = "FriendListFragment"
    }
}