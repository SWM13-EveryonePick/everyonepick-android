package org.soma.everyonepick.login.ui.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import org.soma.everyonepick.foundation.utility.HOME_ACTIVITY_CLASS
import org.soma.everyonepick.login.databinding.FragmentLoginBinding
import org.soma.everyonepick.login.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.onClickLoginButton = View.OnClickListener {
                if(viewModel.isApiLoading.value == true) return@OnClickListener

                viewModel.isApiLoading.value = true
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if(error != null) onLoginFailure()
                    else if(token != null) onLoginSuccess()
                }

                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if(UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                    UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                        if(error != null) {
                            onLoginFailure()

                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도한다.
                            // 단, 사용자가 의도적으로 로그인을 취소한 경우는 제외한다.
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                viewModel.isApiLoading.value = false
                                return@loginWithKakaoTalk
                            }
                            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                        }
                        else if(token != null) onLoginSuccess()
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                }
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
                Log.d(
                    TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                // TODO: 가입 여부 체크
                // 첫 로그인일 경우
                if(true){
                    // TODO: 회원가입 진행
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}