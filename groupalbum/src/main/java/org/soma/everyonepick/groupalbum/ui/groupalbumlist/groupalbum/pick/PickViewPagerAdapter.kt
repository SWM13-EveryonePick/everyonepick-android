package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PickViewPagerAdapter(
    fragment: Fragment,
    private val photoUrlList: List<String>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = photoUrlList.size

    override fun createFragment(position: Int): Fragment {
        return ImageViewFragment(photoUrlList[position])
    }
}