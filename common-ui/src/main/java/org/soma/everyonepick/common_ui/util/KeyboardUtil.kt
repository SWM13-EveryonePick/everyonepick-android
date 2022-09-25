package org.soma.everyonepick.common_ui.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.children

class KeyboardUtil {
    companion object {
        /**
         * 뷰에 터치 시 키보드를 내리는 리스너를 설정합니다. 해당 함수는 재귀함수이며,
         * 모든 하위 뷰에 OnTouchListener를 적용합니다.
         */
        fun setOnTouchListenerToHideKeyboard(view: View, activity: Activity) {
            if (view !is EditText) {
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

        /**
         * @param exceptionViews 예외 뷰들에는 'touch to hide keyboard'가 적용되지 않습니다.
         */
        fun setOnTouchListenerToHideKeyboard(view: View, activity: Activity, exceptionViews: List<View>) {
            if (view in exceptionViews) return

            if (view !is EditText) {
                view.setOnTouchListener { _, _ ->
                    hideKeyboard(activity)
                    false
                }
            }

            if (view is ViewGroup) {
                for (child in view.children) {
                    setOnTouchListenerToHideKeyboard(child, activity, exceptionViews)
                }
            }
        }

        fun hideKeyboard(activity: Activity) {
            activity.currentFocus?.let {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }

        fun showKeyboard(view: EditText, activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            view.requestFocus()
            imm.showSoftInput(view, 0)

            // Set cursor at the end of view
            view.setSelection(view.text.toString().length)
        }
    }
}