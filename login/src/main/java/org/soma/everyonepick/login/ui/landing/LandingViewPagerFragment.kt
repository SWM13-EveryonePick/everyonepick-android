package org.soma.everyonepick.login.ui.landing

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.PreferencesDataStore
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.utility.Util
import org.soma.everyonepick.login.viewmodel.LandingViewPagerViewModel

class LandingViewPagerFragment : Fragment() {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingViewPagerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.onClickNextButton = View.OnClickListener {
                binding.viewpager2.currentItem += 1
            }
            it.onClickLoginButton = View.OnClickListener {
                if(viewModel.isApiLoading.value == true) return@OnClickListener

                viewModel.isApiLoading.value = true
                Util.doKakaoLogin(requireContext(), { _,_ -> onLoginSuccess() }, { _,_ -> onLoginFailure() })
            }
        }

        return binding.root
    }

    private fun onLoginFailure() {
        viewModel.isApiLoading.value = false
        Toast.makeText(requireContext(), "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun onLoginSuccess() {
        UserApiClient.instance.me { user, error ->
            viewModel.isApiLoading.value = false

            if(error != null){
                Toast.makeText(requireContext(), "사용자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }else if(user != null) {
                Log.d(TAG, "사용자 정보 요청 성공\n회원번호: ${user.id}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                // TODO: signUp
                // 성공: token data store에 저장
                lifecycleScope.launch {
                    PreferencesDataStore(requireContext()).editAccessToken("TODO: 토큰값으로 변경")
                    PreferencesDataStore(requireContext()).editRefreshToken("TODO: 토큰값으로 변경")
                }
                // 실패: onLoginFailure()

                // TODO: 얼굴정보가 등록되었는가?
                if(true){
                    findNavController().navigate(
                        LandingViewPagerFragmentDirections.toFaceInformationDescriptionFragment()
                    )
                }else{
                    startHomeActivity()
                }
            }
        }
    }

    private fun startHomeActivity() {
        val intent = Intent(
            requireContext(),
            Class.forName(HOME_ACTIVITY_CLASS)
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = LandingViewPagerAdapter(this)
            it.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.currentPosition.value = it.currentItem
                }
            })
            binding.customindicator.setupViewPager2(it, it.currentItem)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.textTermsdescription.run {
            val spannableStringBuilder = SpannableStringBuilder(text)
            spannableStringBuilder.setSpan(
                object: ClickableSpan() {
                    override fun onClick(widget: View) {
                        // TODO: 서비스 이용약관
                        Log.d(TAG, "서비스 이용약관")
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        highlightColor = Color.TRANSPARENT
                    }
                }, 22, 30, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableStringBuilder.setSpan(
                object: ClickableSpan() {
                    override fun onClick(widget: View) {
                        // TODO: 개인정보 취급방침
                        Log.d(TAG, "개인정보 취급방침")
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        highlightColor = Color.TRANSPARENT
                    }
                }, 32, 41, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

            movementMethod = LinkMovementMethod.getInstance()
            text = spannableStringBuilder
        }
    }

    companion object {
        private const val TAG = "LandingViewPager"
    }
}