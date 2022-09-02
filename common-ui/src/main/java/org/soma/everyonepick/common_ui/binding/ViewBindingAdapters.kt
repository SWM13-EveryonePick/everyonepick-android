package org.soma.everyonepick.common_ui.binding

import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.internal.managers.ViewComponentManager

@BindingAdapter("setVisibility")
fun setVisibility(view: View, flag: Boolean) {
    view.visibility = if (flag) View.VISIBLE else View.GONE
}

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

@BindingAdapter("photoUrl")
fun bindImageView(imageView: ImageView, photoUrl: String) {
    Glide.with(imageView.context)
        .load(photoUrl)
        .into(imageView)
}

@BindingAdapter("photoUrl", "roundingRadius", requireAll = true)
fun bindImageView(imageView: ImageView, photoUrl: String, roundingRadius: Int) {
    Glide.with(imageView.context)
        .load(photoUrl)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))
        .into(imageView)
}

@BindingAdapter("uri")
fun bindImageView(imageView: ImageView, uri: Uri) {
    Glide.with(imageView.context)
        .load(uri)
        .into(imageView)
}

@BindingAdapter("bitmap")
fun bindImageView(imageView: ImageView, bitmap: Bitmap?) {
    imageView.setImageBitmap(bitmap)
}

/**
 * 데이터바인딩으로 스타일을 적용함과 동시에 text 내용에 데이터바인딩 값이 들어갈 경우에는 setText() 이후에 spannable이
 * 적용된다는 보장이 없습니다. 따라서 이곳에서 텍스트까지 한번에 처리함으로써 안전하게 작동하는 것을 보장합니다.
 */
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

@BindingAdapter("text", "boldColorStart", "boldColorEnd", "color", requireAll = true)
fun setTextViewBoldAndColorWithRange(view: TextView, text: String, boldColorStart: Int, boldColorEnd: Int, color: Int) {
    val spannable = SpannableString(text)
    spannable.setSpan(
        ForegroundColorSpan(color),
        boldColorStart, boldColorEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(
        StyleSpan(Typeface.BOLD),
        boldColorStart, boldColorEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    view.text = spannable
}