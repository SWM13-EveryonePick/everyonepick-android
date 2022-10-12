package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentResultPhotoListBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewModel

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
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    /** [ResultPhotoListFragmentListener] */
    override fun onClickDeleteButton() {

    }

    override fun onClickCancelButton() {

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