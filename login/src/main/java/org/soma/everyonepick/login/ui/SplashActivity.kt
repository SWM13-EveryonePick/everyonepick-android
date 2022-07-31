package org.soma.everyonepick.login.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        supportActionBar?.hide()

        startTextViewAnimation()
        finishSplash()
    }

    private fun finishSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }, 500)
    }

    override fun onStart() {
        super.onStart()
        window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            it.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun startTextViewAnimation() {
        val descriptionParams = binding.textDescription.layoutParams as ConstraintLayout.LayoutParams
        ValueAnimator.ofInt(100, 0).apply {
            addUpdateListener { valueAnimator ->
                binding.textTitle.alpha = animatedFraction
                binding.textDescription.alpha = animatedFraction

                descriptionParams.bottomMargin = valueAnimator.animatedValue as Int
                binding.textDescription.layoutParams = descriptionParams
            }
            duration = 300

            start()
        }
    }
}