package org.soma.everyonepick.groupalbum.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumParentViewPagerAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumparentviewpagerBinding
import org.soma.everyonepick.groupalbum.utility.GroupAlbumListMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumParentViewPagerViewModel

private val TAB_ITEMS = listOf("앨범", "친구 목록")

@AndroidEntryPoint
class GroupAlbumParentViewPagerFragment : Fragment() {
    private var _binding: FragmentGroupalbumparentviewpagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumParentViewPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumparentviewpagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumParentViewPagerAdapter(this)
            it.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.currentItem.value = binding.viewpager2.currentItem
                }
            })
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = TAB_ITEMS[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickSelectButton() {
        viewModel.groupAlbumListMode.value =
            if(viewModel.groupAlbumListMode.value == GroupAlbumListMode.NORMAL_MODE.ordinal) GroupAlbumListMode.SELECTION_MODE.ordinal
            else GroupAlbumListMode.NORMAL_MODE.ordinal
    }
}