package org.soma.everyonepick.setting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.common.HomeActivityUtility
import org.soma.everyonepick.setting.R

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivityUtility).showBottomNavigationView()
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }
}