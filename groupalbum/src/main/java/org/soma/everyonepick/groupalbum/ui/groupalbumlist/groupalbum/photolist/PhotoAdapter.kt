package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.photolist

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.databinding.ItemPhotoBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumFragmentDirections

class PhotoAdapter: ListAdapter<PhotoModel, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemPhotoBinding>(parent, R.layout.item_photo)
        val holder = PhotoViewHolder(binding)

        binding.onClickRoot = View.OnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = GroupAlbumFragmentDirections.toPhotoFragment(item.photo.photoUrl)
                binding.root.findNavController().navigate(directions)
            } else {
                binding.checkbox.performTouch()
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photoModel = getItem(position)
        (holder as PhotoViewHolder).bind(photoModel)
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoModel: PhotoModel) {
            binding.photoModel = photoModel
            binding.executePendingBindings()
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