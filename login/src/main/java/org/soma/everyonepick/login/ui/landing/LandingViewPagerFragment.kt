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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.PreferencesDataStore
import org.soma.everyonepick.common.api.Retrofit2Factory
import org.soma.everyonepick.foundation.data.model.ProviderName
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.api.AuthService
import org.soma.everyonepick.login.api.UserService
import org.soma.everyonepick.login.data.model.Jwt
import org.soma.everyonepick.login.data.model.SignUpRequest
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding
import org.soma.everyonepick.login.utility.LoginUtil
import org.soma.everyonepick.login.viewmodel.LandingViewPagerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LandingViewPagerFragment : Fragment() {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewPagerViewModel by viewModels()

    @Inject lateinit var authService: AuthService

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
                LoginUtil.loginWithKakao(requireContext(), { token, _ -> onLoginSuccess(token) }, { _, _ -> onLoginFailure() })
            }
        }

        return binding.root
    }

    private fun onLoginFailure() {
        viewModel.isApiLoading.value = false
        Toast.makeText(requireContext(), "카카오 로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun onLoginSuccess(token: OAuthToken?) {
        signUp(token) {
            navigateToNextPageByFaceInformation(it.everyonepickAccessToken)
            viewModel.isApiLoading.value = false
        }
    }

    private fun signUp(token: OAuthToken?, callback: (jwt: Jwt) -> Unit) {
        lifecycleScope.launch {
            try {
                val req = SignUpRequest(ProviderName.Kakao.name, token?.accessToken.toString())
                val res = authService.signUp(req)

                // 서버 토큰 저장
                PreferencesDataStore(requireContext()).editAccessToken(res.data.everyonepickAccessToken)
                PreferencesDataStore(requireContext()).editRefreshToken(res.data.everyonepickRefreshToken)

                callback.invoke(res.data)
            } catch (e: Exception) {
                Toast.makeText(context, "회원가입에 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                viewModel.isApiLoading.value = false
            }
        }
    }

    private fun navigateToNextPageByFaceInformation(accessToken: String) {
        lifecycleScope.launch {
            try {
                val userService = Retrofit2Factory.create(UserService::class.java, accessToken)
                val user = userService.getUser().data

                // TODO: 얼굴 정보가 등록되어 있는가? if (user.faceInformation != null)
                if (false) {
                    startHomeActivity()
                } else {
                    val directions = LandingViewPagerFragmentDirections.toFaceInformationDescriptionFragment()
                    findNavController().navigate(directions)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "회원정보를 불러오는 데 실패하였습니다. 잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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