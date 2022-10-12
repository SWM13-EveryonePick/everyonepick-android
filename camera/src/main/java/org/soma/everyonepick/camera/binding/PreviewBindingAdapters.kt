package org.soma.everyonepick.camera.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.common_ui.binding.bindImageView

@BindingAdapter("poseModelList", "selectedPoseIndex", requireAll = true)
fun bindPoseImageView(
    view: ImageView,
    poseModelList: MutableList<Pose>,
    selectedPoseIndex: Int?
) {
    var done = false
    selectedPoseIndex?.let {
        poseModelList.elementAtOrNull(it)?.poseUrl?.let { url ->
            bindImageView(view, url)
            done = true
        }
    }

    if (!done) view.setImageResource(0)
}