package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.imagepicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.ImageModel
import org.soma.everyonepick.groupalbum.databinding.ItemImageBinding

class ImageAdapter: ListAdapter<ImageModel, RecyclerView.ViewHolder>(ImageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_image,
            parent,
            false
        ).apply {
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

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
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