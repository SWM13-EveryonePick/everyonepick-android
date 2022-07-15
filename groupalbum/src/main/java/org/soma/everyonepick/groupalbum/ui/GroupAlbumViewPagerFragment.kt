package org.soma.everyonepick.groupalbum.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumViewPagerAdapter
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumviewpagerBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel

private val TAB_ITEMS = listOf("앨범", "친구 목록")

@AndroidEntryPoint
class GroupAlbumViewPagerFragment : Fragment() {
    private var _binding: FragmentGroupalbumviewpagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumviewpagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this)
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
}