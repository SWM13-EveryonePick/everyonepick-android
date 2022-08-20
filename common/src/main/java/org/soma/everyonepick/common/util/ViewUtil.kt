package org.soma.everyonepick.common.util

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class ViewUtil {
    companion object {
        /**
         * TabLayout의 이동을 활성화 및 비활성화 합니다. 이때 ViewPager2 단에서
         * swipe하여 페이지를 변경할 수 있으므로 이에 대한 처리까지 해줍니다.
         * @param enabled 활성화 여부
         * @param viewPager2 [ViewPager2]
         * @param tabLayout [TabLayout]
         */
        fun setTabLayoutEnabled(enabled: Boolean, viewPager2: ViewPager2, tabLayout: TabLayout) {
            viewPager2.isUserInputEnabled = enabled
            val layout = tabLayout.getChildAt(0) as LinearLayout
            for(child in layout.children) child.isClickable = enabled
        }
    }
}

fun View.setVisibility(flag: Boolean) {
    visibility = if (flag) View.VISIBLE else View.GONE
}

fun View.performTouch() {
    val time = SystemClock.uptimeMillis()
    val x = width/2f
    val y = height/2f
    dispatchTouchEvent(MotionEvent.obtain(time, time, MotionEvent.ACTION_DOWN, x, y, 0))
    dispatchTouchEvent(MotionEvent.obtain(time, time, MotionEvent.ACTION_UP, x, y, 0))
}