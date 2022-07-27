package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.item.PhotoItem
import org.soma.everyonepick.groupalbum.databinding.ItemPhotoBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.GroupAlbumViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.viewmodel.PhotoListViewModel

class PhotoAdapter(
    private val parentViewModel: PhotoListViewModel
): ListAdapter<PhotoItem, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
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
            if(!item.isCheckboxVisible) {
                val photoUrl = item.photoDao.photoUrl
                val directions = GroupAlbumViewPagerFragmentDirections.toPhotoFragment(photoUrl)
                binding.root.findNavController().navigate(directions)
            }else{
                binding.checkbox.isChecked = !binding.checkbox.isChecked
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(parentViewModel.photoItemList.value == null) return@setOnCheckedChangeListener

            val position = holder.absoluteAdapterPosition
            parentViewModel.photoItemList.value!![position].isChecked = isChecked
        }
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
            binding.checkbox.visibility = if(photoItem.isCheckboxVisible) View.VISIBLE else View.GONE
            binding.checkbox.isChecked = photoItem.isChecked
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