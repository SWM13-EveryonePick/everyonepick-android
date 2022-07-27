package org.soma.everyonepick.login.ui.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.soma.everyonepick.login.databinding.FragmentLanding1Binding

class Landing1Fragment : Fragment() {
    private var _binding: FragmentLanding1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanding1Binding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onClickNextButton = View.OnClickListener {
                findNavController().navigate(Landing1FragmentDirections.toLanding2Fragment())
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}