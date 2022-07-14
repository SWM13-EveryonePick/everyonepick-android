package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumBinding

class GroupAlbumAdapter: ListAdapter<GroupAlbum, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupAlbumViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_groupalbum,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupAlbum = getItem(position)
        (holder as GroupAlbumViewHolder).bind(groupAlbum)
    }

    class GroupAlbumViewHolder(
        private val binding: ItemGroupalbumBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(groupAlbum: GroupAlbum) {
            binding.textTitle.text = groupAlbum.title
        }
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbum>() {
    override fun areItemsTheSame(oldItem: GroupAlbum, newItem: GroupAlbum): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupAlbum, newItem: GroupAlbum): Boolean {
        return oldItem == newItem
    }
}