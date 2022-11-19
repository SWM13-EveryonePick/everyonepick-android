package org.soma.everyonepick.app.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.app.R
import org.soma.everyonepick.app.databinding.ActivityHomeBinding
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import org.soma.everyonepick.common.util.NotificationUtil
import org.soma.everyonepick.common_ui.util.setVisibility
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.login.ui.LoginActivity
import javax.inject.Inject


/**
 * 하위 모듈들이 접근할 수 없는 코드를 [HomeActivityUtil]을 통해 하위 모듈에 제공합니다.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeActivityUtil, HomeActivityListener {
    private lateinit var binding: ActivityHomeBinding

    private var valueAnimator: ValueAnimator? = null

    @Inject lateinit var dataStoreUseCase: DataStoreUseCase
    @Inject lateinit var userUseCase: UserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityHomeBinding?>(this, R.layout.activity_home).apply {
            listener = this@HomeActivity
        }

        showTutorialAtFirst()
        supportActionBar?.hide()
        initializeNavigation()

        updateFcmDeviceToken()
    }

    private fun showTutorialAtFirst() {
        lifecycleScope.launch {
            dataStoreUseCase.run {
                val hasTutorialShown = hasTutorialShown.first()
                if (hasTutorialShown != true) {
                    binding.layoutTutorial.visibility = View.VISIBLE
                    editHasTutorialShown(true)
                }
            }
        }
    }

    private fun initializeNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomnavigationview.run {
            setupWithNavController(navController)
            addOnItemSelectedListener(navController) { item ->
                val isCamera = item.itemId == R.id.nav_camera
                binding.layoutCameranavigation.setVisibility(isCamera)

                if (isCamera) hideBottomNavigationView()
                else showBottomNavigationView()
            }
        }
    }

    private fun updateFcmDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            try {
                lifecycleScope.launch {
                    val accessToken = dataStoreUseCase.bearerAccessToken.first()!!
                    userUseCase.updateDeviceToken(accessToken, it.result)
                }
            } catch (e: Exception) {}
        }
    }


    /** [HomeActivityUtil] */
    override fun hideBottomNavigationView() {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        val height = binding.bottomnavigationview.height
        animateBottomMargin(binding.bottomnavigationview, params.bottomMargin, -height)
    }

    private fun animateBottomMargin(view: View, start: Int, end: Int) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        if (params.bottomMargin == end) return

        valueAnimator?.cancel()

        valueAnimator = ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                params.bottomMargin = valueAnimator.animatedValue as Int
                view.layoutParams = params
            }
            duration = ANIMATION_DURATION

            start()
        }
    }

    override fun showBottomNavigationView() {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        animateBottomMargin(binding.bottomnavigationview, params.bottomMargin, 0)
    }

    override fun hideCameraNavigation() {
        val params = binding.layoutCameranavigation.layoutParams as ConstraintLayout.LayoutParams
        val height = binding.layoutCameranavigation.height
        animateBottomMargin(binding.layoutCameranavigation, params.bottomMargin, -height)
    }

    override fun showCameraNavigation() {
        val params = binding.layoutCameranavigation.layoutParams as ConstraintLayout.LayoutParams
        animateBottomMargin(binding.layoutCameranavigation, params.bottomMargin, 0)
    }

    override fun showAreYouSureDialog() {
        DialogWithTwoButton.Builder(this)
            .setMessage(getString(R.string.terminate_app, getString(org.soma.everyonepick.common_ui.R.string.app_name)))
            .setPositiveButtonText(getString(org.soma.everyonepick.common_ui.R.string.termination))
            .setOnClickPositiveButton { finish() }
            .build().show()
    }

    override fun startLoginActivity() {
        val intent = Intent(baseContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    override fun navigateToGroupAlbum() {
        binding.bottomnavigationview.selectedItemId = R.id.nav_group_album
    }


    /** [HomeActivityListener] */
    override fun onClickTutorialListener() {
        binding.layoutTutorial.visibility = View.GONE
    }

    override fun onClickGroupAlbumText() {
        binding.bottomnavigationview.selectedItemId = R.id.nav_group_album
    }

    override fun onClickSettingText() {
        binding.bottomnavigationview.selectedItemId = R.id.nav_setting
    }

    companion object {
        private const val ANIMATION_DURATION = 150L
    }
}

interface HomeActivityListener {
    fun onClickTutorialListener()
    fun onClickGroupAlbumText()
    fun onClickSettingText()
}