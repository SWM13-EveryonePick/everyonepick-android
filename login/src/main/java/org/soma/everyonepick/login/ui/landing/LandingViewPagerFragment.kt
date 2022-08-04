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
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.PreferencesDataStore
import org.soma.everyonepick.common.api.Retrofit2Factory
import org.soma.everyonepick.foundation.data.model.ProviderName
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.api.AuthService
import org.soma.everyonepick.login.data.model.SignUpRequest
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.utility.Util
import org.soma.everyonepick.login.viewmodel.LandingViewPagerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LandingViewPagerFragment : Fragment() {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewPagerViewModel by viewModels()

    @Inject
    lateinit var authService: AuthService

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
                Util.loginWithKakao(requireContext(), { token,_ -> onLoginSuccess(token) }, { _,_ -> onLoginFailure() })
            }
        }

        return binding.root
    }

    private fun onLoginFailure() {
        viewModel.isApiLoading.value = false
        Toast.makeText(requireContext(), "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun onLoginSuccess(token: OAuthToken?) {
        lifecycleScope.launch {
            try {
                val req = SignUpRequest(ProviderName.Kakao.name, token?.accessToken.toString())
                val res = authService.signUp(req)

                PreferencesDataStore(requireContext()).editAccessToken(res.data.everyonepickAccessToken)
                PreferencesDataStore(requireContext()).editRefreshToken(res.data.everyonepickRefreshToken)

                UserApiClient.instance.me { user, error ->
                    viewModel.isApiLoading.value = false

                    if (error != null) {
                        Toast.makeText(requireContext(), "사용자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } else if(user != null) {
                        Log.d(TAG, "사용자 정보 요청 성공\n회원번호: ${user.id}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                        // TODO: 얼굴정보가 등록되었는가?
                        if (true) {
                            findNavController().navigate(
                                LandingViewPagerFragmentDirections.toFaceInformationDescriptionFragment()
                            )
                        } else {
                            startHomeActivity()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                onLoginFailure()
                viewModel.isApiLoading.value = false
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