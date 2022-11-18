package org.soma.everyonepick.login.ui.landing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.source.AuthService
import org.soma.everyonepick.common.util.PRIVACY_POLICY_URL
import org.soma.everyonepick.common.util.TERMS_OF_SERVICE_URL
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.common_ui.ScrollableWebViewActivity
import org.soma.everyonepick.common_ui.ScrollableWebViewActivity.Companion.putScrollableWebViewActivityExtras
import org.soma.everyonepick.common_ui.R
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class LandingViewPagerFragment : Fragment(), LandingViewPagerFragmentListener {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewPagerViewModel by viewModels()

    @Inject lateinit var homeActivityClass: Class<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingViewPagerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.listener = this
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                if (it.isNotEmpty()) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = LandingViewPagerAdapter(this)
            it.setOnPageSelectedListener { position -> viewModel.setViewPagerPosition(position) }
            binding.customindicator.setupViewPager2(it, it.currentItem)
        }
    }

    override fun onResume() {
        super.onResume()
        // 이용약관 텍스트 일부 범위(e.g. 개인정보 처리방침)에 클릭 이벤트를 추가합니다.
        binding.textTermsdescription.let {
            it.movementMethod = LinkMovementMethod.getInstance()
            it.text = SpannableStringBuilder(it.text).apply {
                setSpan(
                    object: ClickableSpan() {
                        override fun onClick(widget: View) {
                            val intent = Intent(requireContext(), ScrollableWebViewActivity::class.java)
                                .putScrollableWebViewActivityExtras(getString(R.string.terms_of_service_title), TERMS_OF_SERVICE_URL)
                            startActivity(intent)
                        }
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            it.highlightColor = Color.TRANSPARENT
                        }
                    }, 22, 30, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                setSpan(
                    object: ClickableSpan() {
                        override fun onClick(widget: View) {
                            val intent = Intent(requireContext(), ScrollableWebViewActivity::class.java)
                                .putScrollableWebViewActivityExtras(getString(R.string.privacy_policy_title), PRIVACY_POLICY_URL)
                            startActivity(intent)
                        }
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            it.highlightColor = Color.TRANSPARENT
                        }
                    }, 32, 41, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** LandingViewPagerFragmentListener */
    override fun onClickNextButton() {
        binding.viewpager2.currentItem += 1
    }

    override fun onClickLoginButton() {
        if (viewModel.isApiLoading.value) return

        viewModel.setIsApiLoading(true)
        loginWithKakaoAndSignUp {
            // 친구 목록 API를 호출함으로써 친구 목록 동의('필수 동의'가 아닌 '이용 중 동의' 항목)를 유도합니다.
            viewModel.readFriends {
                navigateToNextPageByFaceInformation()
            }
        }
    }

    private fun loginWithKakaoAndSignUp(onSuccess: () -> Unit) {
        // 카카오톡 로그인
        LoginUtil.loginWithKakao(requireContext(), { token, _ ->
            // 카카오 토큰을 기반으로 자체 서버로 signUp
            viewModel.signUp(token) {
                onSuccess.invoke()
            }
        }, { _, error ->
            viewModel.setIsApiLoading(false)
            Log.e(TAG, error.toString())
            Toast.makeText(requireContext(), getString(org.soma.everyonepick.login.R.string.toast_failed_to_login_with_kakao), Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * 얼굴정보가 등록되어 이미 등록되어 있으면 [HomeActivity], 등록되어 있지 않다면 [FaceInformationDescriptionFragment]로 이동합니다.
     */
    private fun navigateToNextPageByFaceInformation() {
        viewModel.withReadUser { user ->
            // 얼굴 정보가 등록되어 있는가?
            if (user.isRegistered == true) {
                LoginUtil.startHomeActivity(requireActivity(), homeActivityClass)
            } else {
                val directions = LandingViewPagerFragmentDirections.toFaceInformationDescriptionFragment()
                findNavController().navigate(directions)
            }
        }
    }

    companion object {
        private const val TAG = "LandingViewPager"
    }
}

interface LandingViewPagerFragmentListener {
    fun onClickNextButton()
    fun onClickLoginButton()
}