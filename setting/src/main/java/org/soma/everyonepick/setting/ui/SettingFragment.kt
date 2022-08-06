package org.soma.everyonepick.setting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.soma.everyonepick.foundation.util.HomeActivityUtil
import org.soma.everyonepick.setting.R

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as org.soma.everyonepick.foundation.util.HomeActivityUtil).showBottomNavigationView()
    }
}