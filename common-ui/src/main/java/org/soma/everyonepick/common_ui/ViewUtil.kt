package org.soma.everyonepick.common_ui

import android.view.View

fun View.setVisibility(flag: Boolean) {
    visibility = getVisibility(flag)
}
fun View.getVisibility(flag: Boolean) = if (flag) View.VISIBLE else View.GONE