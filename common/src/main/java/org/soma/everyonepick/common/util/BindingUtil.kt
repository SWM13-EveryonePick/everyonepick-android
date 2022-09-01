package org.soma.everyonepick.common.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class BindingUtil {
    companion object {
        fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)
    }
}