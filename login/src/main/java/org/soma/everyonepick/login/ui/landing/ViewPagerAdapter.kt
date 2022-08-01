package org.soma.everyonepick.login.ui.landing

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LandingViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> Landing1Fragment()
            1 -> Landing2Fragment()
            else -> LoginFragment()
        }
    }
}