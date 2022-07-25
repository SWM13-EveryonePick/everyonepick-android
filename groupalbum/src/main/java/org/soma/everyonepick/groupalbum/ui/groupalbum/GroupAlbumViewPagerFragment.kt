package org.soma.everyonepick.groupalbum.ui.groupalbum

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.common.ViewUtility
import org.soma.everyonepick.common.ViewUtility.Companion.setTabLayoutEnabled
import org.soma.everyonepick.groupalbum.adapter.GroupAlbumViewPagerAdapter
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.databinding.FragmentGroupalbumviewpagerBinding
import org.soma.everyonepick.groupalbum.ui.GroupAlbumListFragment.Companion.GROUP_ALBUM_REMOVED
import org.soma.everyonepick.groupalbum.utility.PhotoListMode
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

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(viewModel.photoListMode.value) {
                    PhotoListMode.NORMAL_MODE.ordinal -> findNavController().navigateUp()
                    else -> viewModel.photoListMode.value = PhotoListMode.NORMAL_MODE.ordinal
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

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
            it.adapter = GroupAlbumViewPagerAdapter(this)
            it.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.currentItem.value = it.currentItem
                }
            })
        }
        TabLayoutMediator(binding.tablayout, binding.viewpager2) { tab, position ->
            tab.text = TAB_ITEMS[position]
        }.attach()
    }

    override fun onStart() {
        super.onStart()

        viewModel.photoListMode.observe(viewLifecycleOwner) { photoListMode ->
            setTabLayoutEnabled(
                enabled = photoListMode == PhotoListMode.NORMAL_MODE.ordinal,
                binding.viewpager2,
                binding.tablayout
            )
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


    /** Databinding functions */
    fun onClickBackButton() {
        findNavController().navigateUp()
    }

    fun onClickSelectButton() {
        viewModel.photoListMode.value =
            if(viewModel.photoListMode.value == PhotoListMode.NORMAL_MODE.ordinal) PhotoListMode.SELECTION_MODE.ordinal
            else PhotoListMode.NORMAL_MODE.ordinal
    }

    fun onClickDrawerButton() {
        binding.drawerlayout.openDrawer(GravityCompat.END)
    }

    fun onClickDrawerTitleEditButton() {
        val editText = EditText(context).apply {
            setText(viewModel.groupAlbum.value?.title)
            hint = "예시) 밴드부 동아리"
        }
        AlertDialog.Builder(context)
            .setTitle("단체공유앨범 이름 변경")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                // TODO: API
                viewModel.updateGroupAlbumTitle(editText.text.toString())
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    fun onClickDrawerExitButton() {
        AlertDialog.Builder(context).setMessage("단체공유앨범에서 나갑니다.")
            .setPositiveButton("확인") { _, _ ->
                // TODO: API
                activity?.supportFragmentManager?.setFragmentResult(
                    GROUP_ALBUM_REMOVED,
                    bundleOf("id" to args.groupAlbumId)
                )
                findNavController().navigateUp()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }
}