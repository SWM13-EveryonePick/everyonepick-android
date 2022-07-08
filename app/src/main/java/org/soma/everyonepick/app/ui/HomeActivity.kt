package org.soma.everyonepick.app.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.soma.everyonepick.app.R
import org.soma.everyonepick.app.databinding.ActivityHomeBinding
import org.soma.everyonepick.camera.ui.CameraFragment
import org.soma.everyonepick.groupalbum.ui.GroupAlbumFragment
import org.soma.everyonepick.setting.ui.SettingFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initializeNavigation()
    }

    private fun initializeNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomnavigationview.run{
            setupWithNavController(navController)
            setOnItemSelectedListener { item ->
                val fragment = getFragmentByItemId(item.itemId)
                replaceFragment(fragment)

                // Camera Fragment에서 풀스크린을 사용합니다.
                setFullScreenMode(item.itemId == R.id.cameraFragment)

                return@setOnItemSelectedListener true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, fragment)
            .commit()
    }

    private fun getFragmentByItemId(itemId: Int): Fragment {
        return when(itemId) {
            R.id.cameraFragment -> CameraFragment({
                binding.bottomnavigationview.selectedItemId = R.id.groupAlbumFragment
            },{
                binding.bottomnavigationview.selectedItemId = R.id.settingFragment
            })
            R.id.groupAlbumFragment -> GroupAlbumFragment()
            else -> SettingFragment()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setFullScreenMode(flag: Boolean) {
        supportActionBar?.setShowHideAnimationEnabled(false)

        if(flag) {
            hideStatusBar()
            supportActionBar?.hide()
            binding.bottomnavigationview.visibility = View.GONE
        }else{
            showStatusBar()
            supportActionBar?.show()
            binding.bottomnavigationview.visibility = View.VISIBLE
        }
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