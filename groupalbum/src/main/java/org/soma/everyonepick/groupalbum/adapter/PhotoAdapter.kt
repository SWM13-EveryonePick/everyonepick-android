package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.databinding.ItemPhotoBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist.PhotoListViewModel

class PhotoAdapter(
    private val parentViewModel: PhotoListViewModel
): ListAdapter<PhotoModel, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPhotoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_photo,
            parent,
            false
        )

        val holder = PhotoViewHolder(binding)
        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemPhotoBinding, holder: PhotoViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val photoUrl = item.photo.photoUrl
                val directions = GroupAlbumFragmentDirections.toPhotoFragment(photoUrl)
                binding.root.findNavController().navigate(directions)
            } else {
                binding.checkbox.performTouch()
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            parentViewModel.photoModelList.value?.let { photoModelList ->
                val position = holder.absoluteAdapterPosition
                photoModelList[position].isChecked = isChecked
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photoModel = getItem(position)
        (holder as PhotoViewHolder).bind(photoModel)
    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoModel: PhotoModel) {
            Glide.with(binding.root)
                .load(photoModel.photo.photoUrl)
                .transform(CenterCrop(), RoundedCorners(CORNER_RADIUS))
                .into(binding.imagePhoto)
            binding.checkbox.setVisibility(photoModel.isCheckboxVisible)
            binding.checkbox.isChecked = photoModel.isChecked
        }

        companion object {
            private const val CORNER_RADIUS = 50
        }
    }
}

private class PhotoDiffCallback: DiffUtil.ItemCallback<PhotoModel>() {
    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.photo.id == newItem.photo.id
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem == newItem
    }
}