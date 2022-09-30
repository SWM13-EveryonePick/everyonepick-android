package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentImageViewBinding

class ImageViewFragment(private val photoUrl: String) : Fragment() {

    private var _binding: FragmentImageViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageViewBinding.inflate(inflater, container, false).also {
            it.photoUrl = photoUrl
        }
        return binding.root
    }
}