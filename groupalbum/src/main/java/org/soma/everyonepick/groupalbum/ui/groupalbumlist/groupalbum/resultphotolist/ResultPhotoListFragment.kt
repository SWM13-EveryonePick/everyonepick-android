package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.resultphotolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentResultPhotoListBinding

class ResultPhotoListFragment : Fragment() {

    private var _binding: FragmentResultPhotoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ResultPhotoListFragment()
    }
}