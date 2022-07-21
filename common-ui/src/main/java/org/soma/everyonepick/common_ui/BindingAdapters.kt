package org.soma.everyonepick.common_ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.soma.everyonepick.common_ui.CustomActionBar

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("onClickBackButton")
    fun bindBackButton(view: CustomActionBar, onClickBackButton: View.OnClickListener) {
        view.findViewById<ImageView>(R.id.image_backbutton).setOnClickListener(onClickBackButton)
    }

    @JvmStatic
    @BindingAdapter("photoUrl")
    fun bindImageView(imageView: ImageView, photoUrl: String) {
        Glide.with(imageView.context)
            .load(photoUrl)
            .into(imageView)
    }
}