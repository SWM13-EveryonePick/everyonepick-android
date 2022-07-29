package org.soma.everyonepick.common_ui.binding

import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.internal.managers.ViewComponentManager

object ViewBinding {
    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindBackButton(view: View, onBackPressed: Boolean) {
        var context = view.context
        // For Hilt
        if (context is ViewComponentManager.FragmentContextWrapper) {
            context = (context as ContextWrapper).baseContext
        }
        if (onBackPressed && context is OnBackPressedDispatcherOwner) {
            view.setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("photoUrl")
    fun bindImageView(imageView: ImageView, photoUrl: String) {
        Glide.with(imageView.context)
            .load(photoUrl)
            .into(imageView)
    }

    /**
     * 데이터바인딩으로 스타일을 적용함과 동시에 text 내용에 데이터바인딩 값이 들어갈 경우에는
     * setText() 이후에 spannable이 적용된다는 보장이 없으므로, 이곳에서 텍스트까지 한번에 처리합니다.
     */
    @JvmStatic
    @BindingAdapter("text", "boldStart", "boldEnd", requireAll = true)
    fun setTextViewBoldWithRange(view: TextView, text: String, boldStart: Int, boldEnd: Int) {
        val spannable = SpannableString(text)
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            boldStart, boldEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.text = spannable
    }

    @JvmStatic
    @BindingAdapter("text", "colorStart", "colorEnd", "color", requireAll = true)
    fun setTextViewColorWithRange(view: TextView, text: String, colorStart: Int, colorEnd: Int, color: Int) {
        val spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(color),
            colorStart, colorEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.text = spannable
    }

    @JvmStatic
    @BindingAdapter("prefix", "text", "suffix", "color", requireAll = true)
    fun setTextViewColor(view: TextView, prefix: String, text: String, suffix: String, color: Int) {
        val spannable = SpannableStringBuilder(text)
        spannable.setSpan(
            ForegroundColorSpan(color), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.insert(0, prefix)
        spannable.insert(spannable.length, suffix)
        view.text = spannable
    }
}