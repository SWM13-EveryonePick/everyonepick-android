package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.imagepicker

import android.view.LayoutInflater
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

class ImageAdapter(
    private val parentViewModel: ImagePickerViewModel
): ListAdapter<ImageModel, RecyclerView.ViewHolder>(ImageDiffCallback()) {
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
            binding.checkbox.performTouch()
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            parentViewModel.imageModelList.value?.let { imageModelList ->
                val position = holder.absoluteAdapterPosition
                imageModelList[position].isChecked = isChecked
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageModel = getItem(position)
        (holder as ImageViewHolder).bind(imageModel)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel) {
            Glide.with(binding.root)
                .load(imageModel.uri)
                .into(binding.image)
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