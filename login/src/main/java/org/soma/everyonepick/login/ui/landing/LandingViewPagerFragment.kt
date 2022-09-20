package org.soma.everyonepick.login.ui.landing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.data.entity.ProviderName
import org.soma.everyonepick.common.data.source.AuthService
import org.soma.everyonepick.common.data.dto.SignUpRequest
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.common.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.common_ui.FullTextActivity
import org.soma.everyonepick.common_ui.FullTextActivity.Companion.putFullTextActivityExtras
import org.soma.everyonepick.common_ui.R
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.util.LoginUtil
import javax.inject.Inject

@AndroidEntryPoint
class LandingViewPagerFragment : Fragment(), LandingViewPagerFragmentListener {
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
                            val intent = Intent(requireContext(), FullTextActivity::class.java)
                                .putFullTextActivityExtras(getString(R.string.terms_of_service_title), getString(R.string.terms_of_service_contents))
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
                            val intent = Intent(requireContext(), FullTextActivity::class.java)
                                .putFullTextActivityExtras(getString(R.string.privacy_policy_title), getString(R.string.privacy_policy_contents))
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
        super.onDestroy()
        _binding = null
    }


    /** LandingViewPagerFragmentListener */
    override fun onClickNextButton() {
        binding.viewpager2.currentItem += 1
    }

    /**
     * 1. 카카오톡으로 로그인
     * 2. 카카오 토큰을 기반으로 [AuthService.signUp] 호출
     * 3. 얼굴정보 등록 여부에 따라서, HomeActivity 혹은 FaceInformation 페이지로 이동
     */
    override fun onClickLoginButton() {
        if (viewModel.isApiLoading.value) return

        viewModel.setIsApiLoading(true)
        LoginUtil.loginWithKakao(requireContext(), { token, _ ->
            signUpAndNavigate(token)
        }, { _, _ ->
            viewModel.setIsApiLoading(false)
            Toast.makeText(requireContext(), "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * 회원가입을 시도하고, 성공 시 [navigateToNextPageByFaceInformation]를 호출합니다.
     */
    private fun signUpAndNavigate(token: OAuthToken?) {
        viewModel.signUp(token) {
            navigateToNextPageByFaceInformation()
        }
    }

    /**
     * 얼굴정보가 등록되어 이미 등록되어 있으면 HomeActivity, 등록되어 있지 않다면 FaceInformation으로 이동합니다.
     */
    private fun navigateToNextPageByFaceInformation() {
        viewModel.withReadUser { user ->
            // 얼굴 정보가 등록되어 있는가?
            // TODO: if (user.faceInformation != null)
            if (false) {
                LoginUtil.startHomeActivity(requireActivity())
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