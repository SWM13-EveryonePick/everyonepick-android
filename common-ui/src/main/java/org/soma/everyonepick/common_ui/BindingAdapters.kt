package org.soma.everyonepick.common_ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.soma.everyonepick.common_ui.CustomActionBar

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("onClickBackButton")
    fun bindBackButton(view: CustomActionBar, onClickBackButton: View.OnClickListener) {
        view.findViewById<ImageView>(R.id.image_backbutton).setOnClickListener(onClickBackButton)
    }
}