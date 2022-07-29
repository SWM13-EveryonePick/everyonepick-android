package org.soma.everyonepick.common_ui

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

fun View.setVisibility(flag: Boolean) {
    visibility = getVisibility(flag)
}
fun View.getVisibility(flag: Boolean) = if (flag) View.VISIBLE else View.GONE

fun View.performTouch() {
    val time = SystemClock.uptimeMillis()
    val x = width/2f
    val y = height/2f
    dispatchTouchEvent(MotionEvent.obtain(time, time, MotionEvent.ACTION_DOWN, x, y, 0))
    dispatchTouchEvent(MotionEvent.obtain(time, time, MotionEvent.ACTION_UP, x, y, 0))
}