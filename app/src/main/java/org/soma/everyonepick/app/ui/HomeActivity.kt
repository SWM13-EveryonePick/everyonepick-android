package org.soma.everyonepick.app.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.app.R
import org.soma.everyonepick.app.databinding.ActivityHomeBinding
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.foundation.utility.PREFERENCE_HAS_TUTORIAL_SHOWN


private const val ANIMATION_DURATION = 150L

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeActivityUtility {
    private lateinit var binding: ActivityHomeBinding

    private var valueAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityHomeBinding?>(this, R.layout.activity_home).apply {
            onClickTutorialListener = View.OnClickListener {
                layoutTutorial.visibility = View.GONE
            }
        }

        showTutorialAndEditPreference()
        supportActionBar?.hide()

        initializeNavigation()
    }

    private fun showTutorialAndEditPreference() {
        val pref = getPreferences(Context.MODE_PRIVATE)
        val hasTutorialShown = pref.getBoolean(PREFERENCE_HAS_TUTORIAL_SHOWN, false)
        // 테스트용 코드: hasTutorialShown = true
        if (!hasTutorialShown) {
            binding.layoutTutorial.visibility = View.VISIBLE
            with(pref.edit()) {
                putBoolean(PREFERENCE_HAS_TUTORIAL_SHOWN, true)
                apply()
            }
        }
    }

    private fun initializeNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomnavigationview.run {
            setupWithNavController(navController)
            addOnItemSelectedListener(navController) { item ->
                // Camera Fragment에서 풀스크린을 사용합니다.
                setFullScreenMode(item.itemId == R.id.nav_camera)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setFullScreenMode(flag: Boolean) {
        supportActionBar?.setShowHideAnimationEnabled(false)

        if(flag) hideStatusBar()
        else showStatusBar()
    }

    private fun hideStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun showStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.show(WindowInsets.Type.statusBars())
        }else{
            window.decorView.systemUiVisibility = View.VISIBLE
        }
    }


    override fun hideBottomNavigationView() {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        val height = binding.bottomnavigationview.height
        animateBottomNavigationViewBottomMargin(params.bottomMargin, -height)
    }

    private fun animateBottomNavigationViewBottomMargin(start: Int, end: Int) {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        if(params.bottomMargin == end) return

        valueAnimator?.cancel()

        valueAnimator = ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                params.bottomMargin = valueAnimator.animatedValue as Int
                binding.bottomnavigationview.layoutParams = params
            }
            duration = ANIMATION_DURATION

            start()
        }
    }

    override fun showBottomNavigationView() {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        animateBottomNavigationViewBottomMargin(params.bottomMargin, 0)
    }

    override fun showAreYouSureDialog() {
        AlertDialog.Builder(this).setMessage("${baseContext.getString(org.soma.everyonepick.common.R.string.app_name)}을 종료합니다.")
            .setPositiveButton("확인") { _, _ ->
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }
}