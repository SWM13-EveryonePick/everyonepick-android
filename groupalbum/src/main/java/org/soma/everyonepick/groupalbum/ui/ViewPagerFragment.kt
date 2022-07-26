package org.soma.everyonepick.groupalbum.ui

import android.content.Context
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
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.common.ViewUtility.Companion.setTabLayoutEnabled
import org.soma.everyonepick.groupalbum.databinding.FragmentViewPagerBinding
import org.soma.everyonepick.groupalbum.utility.GroupAlbumListMode
import org.soma.everyonepick.groupalbum.viewmodel.ViewPagerViewModel


private val TAB_ITEMS = listOf("앨범", "친구목록")

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewPagerViewModel by viewModels()

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(viewModel.groupAlbumListMode.value) {
                    GroupAlbumListMode.NORMAL_MODE.ordinal -> (activity as HomeActivityUtility).showAreYouSureDialog()
                    else -> viewModel.groupAlbumListMode.value = GroupAlbumListMode.NORMAL_MODE.ordinal
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = ViewPagerAdapter(this)
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
        binding.tablayout.apply {
            for (i in 0 until tabCount) {
                getTabAt(i)?.let { tab ->
                    val textView = TextView(context)
                    tab.customView = textView

                    textView.let {
                        it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        it.text = tab.text
                        setTabLayoutText(it, i == selectedTabPosition)
                    }
                }
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.customView?.let { setTabLayoutText(it as TextView, true) }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.customView?.let { setTabLayoutText(it as TextView, false) }
                }
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    private fun setTabLayoutText(textView: TextView, isSelected: Boolean) {
        if(isSelected){
            textView.setTextColor(Color.WHITE)
            textView.setTypeface(null, Typeface.BOLD)
        }else{
            textView.setTextColor(ContextCompat.getColor(requireContext(), org.soma.everyonepick.common_ui.R.color.cloud))
            textView.setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun onStart() {
        super.onStart()

        // (해당 프래그먼트에서만) status bar를 투명화하고 뷰를 확장합니다.
        activity?.window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            it.statusBarColor = Color.TRANSPARENT
        }

        viewModel.groupAlbumListMode.observe(viewLifecycleOwner) { groupAlbumListMode ->
            setTabLayoutEnabled(
                enabled = groupAlbumListMode == GroupAlbumListMode.NORMAL_MODE.ordinal,
                binding.viewpager2,
                binding.tablayout
            )
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            it.statusBarColor = Color.WHITE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }


    fun onClickSelectButton() {
        if(viewModel.groupAlbumListMode.value == GroupAlbumListMode.NORMAL_MODE.ordinal) {
            viewModel.groupAlbumListMode.value = GroupAlbumListMode.SELECTION_MODE.ordinal
        }else{
            viewModel.checkAllTrigger.value = viewModel.checkAllTrigger.value?.plus(1)
        }
    }
}