package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.GroupAlbum
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel

class GroupAlbumAdapter: ListAdapter<GroupAlbumItem, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {
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
        val groupAlbumItem = getItem(position)
        (holder as GroupAlbumViewHolder).bind(groupAlbumItem)
    }


    fun setCheckboxVisible() {
        for(i in 0 until itemCount) {
            getItem(i).run {
                isCheckboxVisible = true
                isSelected = false // 이전 선택 상태를 초기화합니다.
            }
        }
    }

    fun setCheckboxGone() {
        for(i in 0 until itemCount) {
            getItem(i).isCheckboxVisible = false
        }
    }


    class GroupAlbumViewHolder(
        private val binding: ItemGroupalbumBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(groupAlbumItem: GroupAlbumItem) {
            binding.textTitle.text = groupAlbumItem.groupAlbum.title
            binding.checkbox.visibility = if(groupAlbumItem.isCheckboxVisible) View.VISIBLE else View.GONE
            binding.checkbox.isChecked = groupAlbumItem.isSelected
        }
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbumItem>() {
    override fun areItemsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem.groupAlbum.id == newItem.groupAlbum.id && oldItem.isSelected == newItem.isSelected && oldItem.isCheckboxVisible == newItem.isCheckboxVisible
    }

    override fun areContentsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem == newItem
    }
}