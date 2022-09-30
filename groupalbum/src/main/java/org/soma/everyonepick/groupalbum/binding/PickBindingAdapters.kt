package org.soma.everyonepick.groupalbum.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.soma.everyonepick.common_ui.binding.bindImageView

@BindingAdapter("photoUrlList", "index", requireAll = true)
fun bindPickImageView(view: ImageView, photoUrlList: List<String>, index: Int) {
    if (index <= photoUrlList.size - 1) {
        bindImageView(view, photoUrlList[index])
    }
}