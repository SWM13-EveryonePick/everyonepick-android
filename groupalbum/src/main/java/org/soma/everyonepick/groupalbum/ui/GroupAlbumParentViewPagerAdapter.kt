package org.soma.everyonepick.groupalbum.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.groupalbum.ui.FriendListFragment
import org.soma.everyonepick.groupalbum.ui.GroupAlbumListFragment

class GroupAlbumParentViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> GroupAlbumListFragment()
            else -> FriendListFragment()
        }
    }
}