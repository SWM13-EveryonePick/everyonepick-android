package org.soma.everyonepick.login.ui.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import org.soma.everyonepick.login.R
import org.soma.everyonepick.login.databinding.FragmentLandingViewPagerBinding

class LandingViewPagerFragment : Fragment() {
    private var _binding: FragmentLandingViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingViewPagerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onClickNextButton = View.OnClickListener {
                binding.viewpager2.currentItem += 1
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager2.let {
            it.adapter = LandingViewPagerAdapter(this)
        }

        TabLayoutMediator(binding.tablayout, binding.viewpager2) { _, _ -> } .attach()
    }
}