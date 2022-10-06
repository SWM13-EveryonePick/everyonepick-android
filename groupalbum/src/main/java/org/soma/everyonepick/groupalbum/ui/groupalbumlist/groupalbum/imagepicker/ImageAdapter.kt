package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.imagepicker

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.ImageModel
import org.soma.everyonepick.groupalbum.databinding.ItemImageBinding

class ImageAdapter: ListAdapter<ImageModel, RecyclerView.ViewHolder>(ImageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemImageBinding>(parent, R.layout.item_image).apply {
            onClickRoot = View.OnClickListener {
                checkbox.performTouch()
            }
        }
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageModel = getItem(position)
        (holder as ImageViewHolder).bind(imageModel)
    }

    class ImageViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel) {
            binding.imageModel = imageModel
            binding.executePendingBindings()
        }
    }
}

private class ImageDiffCallback: DiffUtil.ItemCallback<ImageModel>() {
    override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem == newItem
    }
}