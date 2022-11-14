package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.soma.everyonepick.common_ui.DialogWithTwoButton
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.FragmentResultPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel
import org.soma.everyonepick.groupalbum.util.SelectionMode

@AndroidEntryPoint
class ResultPhotoListFragment : Fragment(), ResultPhotoListFragmentListener {

    private var _binding: FragmentResultPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultPhotoListViewModel by viewModels()
    private val parentViewModel: GroupAlbumViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultPhotoListBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.adapter = ResultPhotoAdapter(parentViewModel)
            it.parentViewModel = parentViewModel
            it.listener = this
        }

        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    parentViewModel.resultPhotoSelectionMode.collectLatest { selectionMode ->
                        viewModel.setIsCheckboxVisible(selectionMode == SelectionMode.SELECTION_MODE.ordinal)
                    }
                }

                launch {
                    parentViewModel.groupAlbum.collect {
                        viewModel.readResultPhotoModelList(it.id)
                    }
                }

                launch {
                    viewModel.toastMessage.collectLatest {
                        if (it.isNotEmpty()) {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        parentViewModel.setResultPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }

    override fun onResume() {
        super.onResume()
        viewModel.readResultPhotoModelList(parentViewModel.groupAlbum.value.id)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** [ResultPhotoListFragmentListener] */
    override fun onClickDeleteButton() {
        DialogWithTwoButton.Builder(requireContext())
            .setMessage(getString(R.string.dialog_delete_result_photo))
            .setOnClickPositiveButton {
                viewModel.deleteCheckedResultPhotoList(parentViewModel.groupAlbum.value.id)
                parentViewModel.setResultPhotoSelectionMode(SelectionMode.NORMAL_MODE)
            }
            .build().show()
    }

    override fun onClickCancelButton() {
        parentViewModel.setResultPhotoSelectionMode(SelectionMode.NORMAL_MODE)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ResultPhotoListFragment()
    }
}

interface ResultPhotoListFragmentListener {
    fun onClickDeleteButton()
    fun onClickCancelButton()
}