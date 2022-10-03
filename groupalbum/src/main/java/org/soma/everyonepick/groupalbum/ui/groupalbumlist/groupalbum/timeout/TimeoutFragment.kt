package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.timeout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.common_ui.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.FragmentTimeoutBinding

class TimeoutFragment : Fragment() {

    private var _binding: FragmentTimeoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        KeyboardUtil.showKeyboard(binding.edittextHour, requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}