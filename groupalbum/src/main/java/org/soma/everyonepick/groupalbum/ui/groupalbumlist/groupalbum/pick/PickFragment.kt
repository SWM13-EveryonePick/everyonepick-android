package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.util.ViewUtil.Companion.setOnPageSelectedListener
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentPickBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListFragment

/**
 * 마음에 드는 사진을 선택하는 프래그먼트입니다. 이 프래그먼트는 [PhotoListFragment]에서 여러 사진을 선택하여 합성 작업을
 * 시작하 시점과, 합성중 탭에서 내가 Pick하지 않은 사진을 눌렀을 때 실행되며, [PickFragmentType]로 구분하여 각기 다른
 * 작업을 수행합니다.
 */
class PickFragment : Fragment(), PickFragmentListener {

    private var _binding: FragmentPickBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PickViewModel by viewModels()
    private val args: PickFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPhotoModelListByPhotoList(args.photoIdList.toList(), args.photoUrlList.toList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = PickPhotoAdapter()
            it.listener = this
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.recyclerviewPickphoto)

        binding.recyclerviewPickphoto.apply {
            val snapView = pagerSnapHelper.findSnapView(layoutManager)
            val position = if (snapView == null) 0 else layoutManager?.getPosition(snapView)?: 0
            binding.customindicator.setupRecyclerView(this, pagerSnapHelper, args.photoIdList.count(), position)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /** [PickFragmentListener] */
    override fun onClickNoPickButton() {
        when (args.pickFragmentType) {
            PickFragmentType.TO_CREATE -> navigateToTimeout()
            else -> {
                // TODO: 노선택 API 호출
                findNavController().navigateUp()
            }
        }
    }

    override fun onClickPickCompleteButton() {
        if (viewModel.checked.value > viewModel.maxPickCount) {
            Toast.makeText(requireContext(), getString(R.string.toast_exceed_selection, viewModel.maxPickCount), Toast.LENGTH_SHORT).show()
        } else {
            when (args.pickFragmentType) {
                PickFragmentType.TO_CREATE -> navigateToTimeout()
                else -> {
                    // TODO: 선택 API 호출
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun navigateToTimeout() {
        val selectedPhotoIdList = viewModel.getSelectedPhotoIdList().toLongArray()
        val directions = PickFragmentDirections.toTimeoutFragment(args.groupAlbumId, args.photoIdList, selectedPhotoIdList)
        findNavController().navigate(directions)
    }
}

enum class PickFragmentType { TO_CREATE, TO_SELECT }

interface PickFragmentListener {
    fun onClickNoPickButton()
    fun onClickPickCompleteButton()
}