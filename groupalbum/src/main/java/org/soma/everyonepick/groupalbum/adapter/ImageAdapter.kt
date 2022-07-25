package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.ImageItem
import org.soma.everyonepick.groupalbum.databinding.ItemImageBinding
import org.soma.everyonepick.groupalbum.viewmodel.ImagePickerViewModel

class ImageAdapter(
    private val parentViewModel: ImagePickerViewModel
): ListAdapter<ImageItem, RecyclerView.ViewHolder>(ImageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_image,
            parent,
            false
        )

        val holder = ImageViewHolder(binding)
        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemImageBinding, holder: ImageViewHolder) {
        binding.root.setOnClickListener {
            binding.checkbox.isChecked = !binding.checkbox.isChecked
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(parentViewModel.imageItemList.value == null) return@setOnCheckedChangeListener

            val position = holder.absoluteAdapterPosition
            parentViewModel.imageItemList.value!![position].isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageItem = getItem(position)
        (holder as ImageViewHolder).bind(imageItem)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: ImageItem) {
            Glide.with(binding.root)
                .load(imageItem.uri)
                .into(binding.image)
        }
    }
}

private class ImageDiffCallback: DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }
}