package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumBinding
import org.soma.everyonepick.groupalbum.ui.GroupAlbumParentViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel

class GroupAlbumAdapter(
    val parentViewModel: GroupAlbumListViewModel
): ListAdapter<GroupAlbumItem, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemGroupalbumBinding?>(
            LayoutInflater.from(parent.context),
            R.layout.item_groupalbum,
            parent,
            false
        )
        val holder = GroupAlbumViewHolder(binding)

        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemGroupalbumBinding, holder: GroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            if(parentViewModel.groupAlbumItemList.value == null) return@setOnClickListener

            val position = holder.absoluteAdapterPosition
            // 일반 모드일 때
            if(binding.checkbox.visibility == View.GONE) {
                val direction =
                    GroupAlbumParentViewPagerFragmentDirections.actionGroupalbumparentviewpagerToGroupalbum(
                        getItem(position).groupAlbumDao.id
                    )
                binding.root.findNavController().navigate(direction)
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(parentViewModel.groupAlbumItemList.value == null) return@setOnCheckedChangeListener

            val position = holder.absoluteAdapterPosition
            parentViewModel.groupAlbumItemList.value!![position].isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupAlbumItem = getItem(position)
        (holder as GroupAlbumViewHolder).bind(groupAlbumItem)
    }

    class GroupAlbumViewHolder(
        private val binding: ItemGroupalbumBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(groupAlbumItem: GroupAlbumItem) {
            binding.textTitle.text = groupAlbumItem.groupAlbumDao.title
            binding.checkbox.visibility = if(groupAlbumItem.isCheckboxVisible) View.VISIBLE else View.GONE
            binding.checkbox.isChecked = groupAlbumItem.isChecked
        }
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbumItem>() {
    override fun areItemsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem.groupAlbumDao.id == newItem.groupAlbumDao.id
    }

    override fun areContentsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem == newItem
    }
}