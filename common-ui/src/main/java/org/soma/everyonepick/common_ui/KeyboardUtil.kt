package org.soma.everyonepick.common_ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.children

class KeyboardUtil {
    companion object {
        /**
         * 뷰에 터치 시 키보드를 내리는 리스너를 설정합니다. 해당 함수는 재귀함수이며,
         * 모든 하위 뷰에 OnTouchListener를 적용합니다.
         */
        fun setOnTouchListenerToHideKeyboard(view: View, activity: Activity) {
            if (!view.hasOnClickListeners()) {
                view.setOnTouchListener { _, _ ->
                    hideKeyboard(activity)
                    false
                }
            }

            if (view is ViewGroup) {
                for (child in view.children) {
                    setOnTouchListenerToHideKeyboard(child, activity)
                }
            }
        }

        fun hideKeyboard(activity: Activity) {
            activity.currentFocus?.let {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                it.clearFocus()
            }
        }
    }
}