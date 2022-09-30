package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPickBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment

/**
 * 마음에 드는 사진을 선택하는 프래그먼트입니다. 진입점은, [PhotoListFragment]에서 여러 사진을 선택하여 합성 작업을 시작하는
 * 시점과, 합성중 탭에서 내가 Pick하지 않은 사진을 눌렀을 때입니다. TODO: Type 관련 주석
 */
class PickFragment : Fragment() {

    private var _binding: FragmentPickBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PickViewModel by viewModels()

    private val args: PickFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickBinding.inflate(inflater, container, false)

        viewModel.setPhotoModelListByPhotoList(args.photoIdList.toList(), args.photoUrlList.toList())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = PickViewPagerAdapter(this, viewModel)
            binding.customindicator.setupViewPager2(it, it.currentItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

enum class PickFragmentType { TO_CREATE, TO_SELECT }