package org.soma.everyonepick.app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.soma.everyonepick.app.databinding.ActivityHomeBinding
import org.soma.everyonepick.camera.CameraFragment
import org.soma.everyonepick.groupalbum.GroupAlbumFragment
import org.soma.everyonepick.setting.SettingFragment

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

    private fun setFullScreenMode(flag: Boolean) {
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
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun showStatusBar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(true)
            window.insetsController?.show(WindowInsets.Type.statusBars())
        }else{
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}