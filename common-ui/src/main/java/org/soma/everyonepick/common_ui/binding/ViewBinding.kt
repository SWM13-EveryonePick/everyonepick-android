package org.soma.everyonepick.common_ui.binding

import android.content.ContextWrapper
import android.widget.ImageView
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.internal.managers.ViewComponentManager
import org.soma.everyonepick.common_ui.CustomActionBar

object ViewBinding {
    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindBackButton(view: CustomActionBar, onBackPressed: Boolean) {
        var context = view.context
        // For Hilt
        if(context is ViewComponentManager.FragmentContextWrapper){
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
}