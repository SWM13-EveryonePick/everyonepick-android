package org.soma.everyonepick.groupalbum.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem

abstract class ParentGroupAlbumViewHolder(
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(groupAlbumItem: GroupAlbumItem)
}