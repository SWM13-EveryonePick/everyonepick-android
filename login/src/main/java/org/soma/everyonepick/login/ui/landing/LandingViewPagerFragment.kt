package org.soma.everyonepick.login.ui.landing

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
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.pref.PreferencesDataStore
import org.soma.everyonepick.foundation.data.model.ProviderName
import org.soma.everyonepick.common.api.AuthService
import org.soma.everyonepick.foundation.data.model.SignUpRequest
import org.soma.everyonepick.common.data.repository.UserRepository
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.utility.LoginUtil
import org.soma.everyonepick.login.viewmodel.LandingViewPagerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LandingViewPagerFragment : Fragment(), LandingViewPagerFragmentListener {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewPagerViewModel by viewModels()

    @Inject lateinit var authService: AuthService
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var preferencesDataStore: PreferencesDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingViewPagerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.listener = this
        }

        return binding.root
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
        // 이용약관 텍스트 일부 범위(e.g. 개인정보 처리방침)에 클릭 이벤트를 추가합니다.
        binding.textTermsdescription.let {
            it.movementMethod = LinkMovementMethod.getInstance()
            it.text = SpannableStringBuilder(it.text).apply {
                setSpan(
                    object: ClickableSpan() {
                        override fun onClick(widget: View) {
                            // TODO: 서비스 이용약관
                            Log.d(TAG, "서비스 이용약관")
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
                            // TODO: 개인정보 취급방침
                            Log.d(TAG, "개인정보 취급방침")
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
        if (viewModel.isApiLoading.value == true) return

        viewModel.isApiLoading.value = true
        LoginUtil.loginWithKakao(requireContext(), { token, _ ->
            lifecycleScope.launch {
                signUpAndNavigate(token)
            }
        }, { _, _ ->
            viewModel.isApiLoading.value = false
            Toast.makeText(requireContext(), "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        })
    }

    private suspend fun signUpAndNavigate(token: OAuthToken?) {
        try {
            val data = authService.signUp(SignUpRequest(ProviderName.Kakao.name, token?.accessToken.toString())).data
            preferencesDataStore.editAccessToken(data.everyonepickAccessToken)
            preferencesDataStore.editRefreshToken(data.everyonepickRefreshToken)

            navigateToNextPageByFaceInformation(data.everyonepickAccessToken)
        } catch (e: Exception) {
            Toast.makeText(context, "회원가입에 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            viewModel.isApiLoading.value = false
        }
    }

    private suspend fun navigateToNextPageByFaceInformation(accessToken: String) {
        try {
            val data = userRepository.getUser(accessToken).data

            // 얼굴 정보가 등록되어 있는가?
            // TODO: if (data.faceInformation != null)
            if (false) {
                LoginUtil.startHomeActivity(requireActivity())
            } else {
                val directions = LandingViewPagerFragmentDirections.toFaceInformationDescriptionFragment()
                findNavController().navigate(directions)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "회원정보를 불러오는 데 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            viewModel.isApiLoading.value = false
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