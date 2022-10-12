package org.soma.everyonepick.setting.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.common.util.HomeActivityUtil
import org.soma.everyonepick.setting.R
import org.soma.everyonepick.setting.databinding.FragmentNotificationSettingBinding

class NotificationSettingFragment : Fragment() {
    private var _binding: FragmentNotificationSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationSettingBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivityUtil).hideBottomNavigationView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}