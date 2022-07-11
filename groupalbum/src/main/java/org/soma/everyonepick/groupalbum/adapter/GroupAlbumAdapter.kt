package org.soma.everyonepick.groupalbum.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.soma.everyonepick.groupalbum.ui.FriendFragment
import org.soma.everyonepick.groupalbum.ui.GroupAlbumFragment

class GroupAlbumAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> GroupAlbumFragment()
            else -> FriendFragment()
        }
    }
}