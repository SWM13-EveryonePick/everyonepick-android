package org.soma.everyonepick.groupalbum.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.soma.everyonepick.groupalbum.R

@BindingAdapter("profileThumbnailImage")
fun bindProfileThumbnailImage(imageView: ImageView, profileThumbnailImage: String?) {
    val toLoad = if (profileThumbnailImage.isNullOrEmpty()) R.drawable.default_profile_thumbnail_image else profileThumbnailImage
    Glide.with(imageView.context)
        .load(toLoad)
        .into(imageView)
}