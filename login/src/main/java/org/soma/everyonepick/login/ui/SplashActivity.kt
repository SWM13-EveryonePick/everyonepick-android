package org.soma.everyonepick.login.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishSplash()
    }

    private fun finishSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }, 500)
    }
}