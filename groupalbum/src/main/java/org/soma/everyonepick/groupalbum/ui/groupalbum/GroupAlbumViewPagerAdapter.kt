package org.soma.everyonepick.groupalbum.ui.groupalbum

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.groupalbum.ui.groupalbum.photo.PhotoListFragment

class GroupAlbumViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PhotoListFragment()
            1 -> PhotoListFragment()
            else -> PhotoListFragment()
        }
    }
}