package org.soma.everyonepick.groupalbum.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.util.ViewUtil.Companion.setTabLayoutEnabled
import org.soma.everyonepick.foundation.util.HomeActivityUtil
import org.soma.everyonepick.groupalbum.databinding.FragmentHomeViewPagerBinding
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.viewmodel.HomeViewPagerViewModel

@AndroidEntryPoint
class HomeViewPagerFragment : Fragment(), TabLayout.OnTabSelectedListener {
    private var _binding: FragmentHomeViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewPagerViewModel by viewModels()

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.onClickSelectButtonListener = View.OnClickListener {
                if (viewModel.selectionMode.value == SelectionMode.NORMAL_MODE.ordinal) {
                    viewModel.selectionMode.value = SelectionMode.SELECTION_MODE.ordinal
                } else {
                    viewModel.checkAllTrigger.value = viewModel.checkAllTrigger.value?.plus(1)
                }
            }
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            // 선택 모드일 때는 TabLayout을 비활성화 합니다.
            setTabLayoutEnabled(
                enabled = selectionMode == SelectionMode.NORMAL_MODE.ordinal,
                binding.viewpager2,
                binding.tablayout
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = HomeViewPagerAdapter(this)
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

        // 선택된 탭과 그렇지 않은 탭의 텍스트 스타일에 각각 변화를 줍니다.
        binding.tablayout.let { tabLayout ->
            for (i in 0 until tabLayout.tabCount) {
                tabLayout.getTabAt(i)?.let { tab ->
                    val textView = TextView(context)
                    tab.customView = textView

                    textView.let {
                        it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        it.text = tab.text
                        setTabLayoutText(it, i == tabLayout.selectedTabPosition)
                    }
                }
            }

            tabLayout.addOnTabSelectedListener(this)
        }
    }

    private fun setTabLayoutText(textView: TextView, isSelected: Boolean) {
        // 색상 변경
        textView.setTextColor(
            if (isSelected) Color.WHITE
            else ContextCompat.getColor(requireContext(), org.soma.everyonepick.common_ui.R.color.cloud)
        )
        // 스타일 변경
        textView.setTypeface(
            Typeface.DEFAULT_BOLD,
            if (isSelected) Typeface.BOLD else Typeface.NORMAL
        )
    }

    override fun onStart() {
        super.onStart()

        // (해당 프래그먼트에서만) status bar를 투명화하고 뷰를 확장합니다.
        activity?.window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            it.statusBarColor = Color.TRANSPARENT
        }

        (activity as HomeActivityUtil).showBottomNavigationView()
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(viewModel.selectionMode.value) {
                    SelectionMode.NORMAL_MODE.ordinal -> (activity as HomeActivityUtil).showAreYouSureDialog()
                    else -> viewModel.selectionMode.value = SelectionMode.NORMAL_MODE.ordinal
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            it.statusBarColor = Color.WHITE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /** OnTabSelectedListener */
    override fun onTabSelected(tab: TabLayout.Tab) {
        tab.customView?.let { setTabLayoutText(it as TextView, true) }
    }
    override fun onTabUnselected(tab: TabLayout.Tab) {
        tab.customView?.let { setTabLayoutText(it as TextView, false) }
    }
    override fun onTabReselected(tab: TabLayout.Tab) {}


    companion object {
        private val TAB_ITEMS = listOf("앨범", "친구목록")
    }
}