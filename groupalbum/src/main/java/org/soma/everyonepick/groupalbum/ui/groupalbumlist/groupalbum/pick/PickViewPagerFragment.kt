package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.databinding.FragmentPickViewPagerBinding
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel

class PickViewPagerFragment(
    private val index: Int,
    private val photoModel: PhotoModel
) : Fragment() {

    private var _binding: FragmentPickViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickViewPagerBinding.inflate(inflater, container, false).also {
            it.photoModel = photoModel
            it.index = index
        }
        return binding.root
    }
}