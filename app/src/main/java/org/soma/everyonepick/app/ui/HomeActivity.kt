package org.soma.everyonepick.app.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.app.R
import org.soma.everyonepick.app.databinding.ActivityHomeBinding
import org.soma.everyonepick.common.HomeActivityUtility


private const val ANIMATION_DURATION = 150L

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeActivityUtility {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        supportActionBar?.hide()

        initializeNavigation()
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
        val height = binding.bottomnavigationview.height
        animateBottomNavigationViewBottomMargin(0, -height)
    }

    private fun animateBottomNavigationViewBottomMargin(start: Int, end: Int) {
        val params = binding.bottomnavigationview.layoutParams as ConstraintLayout.LayoutParams
        if(params.bottomMargin == end) return

        ValueAnimator.ofInt(start, end).apply {
            addUpdateListener { valueAnimator ->
                params.bottomMargin = valueAnimator.animatedValue as Int
                binding.bottomnavigationview.layoutParams = params
            }
            duration = ANIMATION_DURATION

            start()
        }
    }

    override fun showBottomNavigationView() {
        val height = binding.bottomnavigationview.height
        animateBottomNavigationViewBottomMargin(-height, 0)
    }
}