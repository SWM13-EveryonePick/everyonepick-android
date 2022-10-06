package org.soma.everyonepick.login.ui.landing

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.login.ui.landing.viewpagerfragment.Landing1Fragment
import org.soma.everyonepick.login.ui.landing.viewpagerfragment.Landing2Fragment
import org.soma.everyonepick.login.ui.landing.viewpagerfragment.LoginFragment

class LandingViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> Landing1Fragment.newInstance()
            1 -> Landing2Fragment.newInstance()
            else -> LoginFragment.newInstance()
        }
    }
}