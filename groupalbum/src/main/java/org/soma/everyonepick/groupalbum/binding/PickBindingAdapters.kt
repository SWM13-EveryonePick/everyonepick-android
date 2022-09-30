package org.soma.everyonepick.groupalbum.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.soma.everyonepick.common_ui.binding.bindImageView
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel

@BindingAdapter("photoModelList", "index", requireAll = true)
fun bindPickImageView(view: ImageView, photoModelList: List<PhotoModel>, index: Int) {
    if (index <= photoModelList.size - 1) {
        bindImageView(view, photoModelList[index].photo.photoUrl)
    }
}