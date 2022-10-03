package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.picklist.PickListFragment

class GroupAlbumViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PhotoListFragment()
            1 -> PickListFragment.newInstance()
            else -> PhotoListFragment()
        }
    }
}