package org.soma.everyonepick.groupalbum.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.groupalbum.ui.friend.FriendListFragment
import org.soma.everyonepick.groupalbum.ui.groupalbum.GroupAlbumListFragment

class GroupAlbumViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> GroupAlbumListFragment()
            else -> FriendListFragment()
        }
    }
}