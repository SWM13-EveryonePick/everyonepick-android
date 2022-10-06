package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pick

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemPickPhotoBinding
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoDiffCallback

/**
 * [PhotoDiffCallback]를 공유합니다.
 */
class PickPhotoAdapter: ListAdapter<PhotoModel, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemPickPhotoBinding>(parent, R.layout.item_pick_photo)
        val holder = PickPhotoViewHolder(binding)

        binding.onClickRoot = View.OnClickListener {
            binding.checkbox.performTouch()
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photoModel = getItem(position)
        (holder as PickPhotoViewHolder).bind(photoModel)
    }

    class PickPhotoViewHolder(private val binding: ItemPickPhotoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoModel: PhotoModel) {
            binding.photoModel = photoModel
            binding.executePendingBindings()
        }
    }
}