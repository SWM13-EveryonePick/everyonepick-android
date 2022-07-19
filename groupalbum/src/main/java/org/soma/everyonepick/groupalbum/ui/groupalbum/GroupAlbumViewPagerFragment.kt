package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumViewPagerAdapter
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumviewpagerBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel
import javax.inject.Inject

private val TAB_ITEMS = listOf("사진", "합성중", "합성완료")

@AndroidEntryPoint
class GroupAlbumViewPagerFragment: Fragment() {
    @Inject
    lateinit var groupAlbumRepository: GroupAlbumRepository

    private var _binding: FragmentGroupalbumviewpagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroupAlbumViewPagerViewModel by viewModels()
    private val args: GroupAlbumViewPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupalbumviewpagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel

        viewModel.groupAlbum.value = groupAlbumRepository.getGroupAlbumDao(args.groupAlbumId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = GroupAlbumViewPagerAdapter(this, args.groupAlbumId)
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = TAB_ITEMS[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun onClickBackButton() {
        findNavController().navigateUp()
    }
}