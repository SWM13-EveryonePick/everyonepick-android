package org.soma.everyonepick.app.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarMenu
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.app.R
import org.soma.everyonepick.app.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
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
            setOnItemSelectedListener { item ->
                // Camera Fragment에서 풀스크린을 사용합니다.
                setFullScreenMode(item.itemId == R.id.nav_camera)

                return@setOnItemSelectedListener NavigationUI.onNavDestinationSelected(
                    item,
                    navController
                )
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
}