package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PickViewPagerAdapter(
    fragment: Fragment,
    private val viewModel: PickViewModel
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = viewModel.photoModelList.value.size

    override fun createFragment(position: Int): Fragment {
        return PickViewPagerFragment(position, viewModel)
    }
}