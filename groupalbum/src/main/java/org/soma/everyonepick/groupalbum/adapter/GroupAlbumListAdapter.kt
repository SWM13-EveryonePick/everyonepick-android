package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.GroupAlbumListItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumlistBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel

class GroupAlbumListAdapter(
    val parentViewModel: GroupAlbumListViewModel
): ListAdapter<GroupAlbumListItem, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemGroupalbumlistBinding?>(
            LayoutInflater.from(parent.context),
            R.layout.item_groupalbumlist,
            parent,
            false
        )
        val holder = GroupAlbumViewHolder(binding)

        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemGroupalbumlistBinding, holder: GroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            if(parentViewModel.groupAlbumListItemList.value == null) return@setOnClickListener

            val position = holder.absoluteAdapterPosition
            // 일반 모드일 때
            if(binding.checkbox.visibility == View.GONE) {

            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(parentViewModel.groupAlbumListItemList.value == null) return@setOnCheckedChangeListener

            val position = holder.absoluteAdapterPosition
            parentViewModel.groupAlbumListItemList.value!![position].isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupAlbumListItem = getItem(position)
        (holder as GroupAlbumViewHolder).bind(groupAlbumListItem)
    }

    class GroupAlbumViewHolder(
        private val binding: ItemGroupalbumlistBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(groupAlbumListItem: GroupAlbumListItem) {
            binding.textTitle.text = groupAlbumListItem.groupAlbum.title
            binding.checkbox.visibility = if(groupAlbumListItem.isCheckboxVisible) View.VISIBLE else View.GONE
            binding.checkbox.isChecked = groupAlbumListItem.isChecked
        }
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbumListItem>() {
    override fun areItemsTheSame(oldItem: GroupAlbumListItem, newItem: GroupAlbumListItem): Boolean {
        return oldItem.groupAlbum.id == newItem.groupAlbum.id
    }

    override fun areContentsTheSame(oldItem: GroupAlbumListItem, newItem: GroupAlbumListItem): Boolean {
        return oldItem == newItem
    }
}