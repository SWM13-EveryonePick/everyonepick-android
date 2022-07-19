package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.PhotoItem
import org.soma.everyonepick.groupalbum.databinding.ItemPhotoBinding

class PhotoAdapter: ListAdapter<PhotoItem, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
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
        // TODO: 사진 뷰어로 이동
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photoItem = getItem(position)
        (holder as PhotoViewHolder).bind(photoItem)
    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoItem: PhotoItem) {
            Glide.with(binding.root)
                .load(photoItem.photoDao.photoUrl)
                .into(binding.imagePhoto)
        }
    }
}

private class PhotoDiffCallback: DiffUtil.ItemCallback<PhotoItem>() {
    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.photoDao.id == newItem.photoDao.id
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem == newItem
    }
}