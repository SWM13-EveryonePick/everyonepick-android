package org.soma.everyonepick.groupalbum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemCreategroupalbumBinding
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumBinding
import org.soma.everyonepick.groupalbum.ui.GroupAlbumParentViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.utility.GroupAlbumViewType
import org.soma.everyonepick.groupalbum.viewholder.CreateGroupAlbumViewHolder
import org.soma.everyonepick.groupalbum.viewholder.GroupAlbumViewHolder
import org.soma.everyonepick.groupalbum.viewholder.ParentGroupAlbumViewHolder
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumListViewModel

/**
 * 가장 마지막 Item을 CreateGroupAlbumItem으로 설정합니다.
 * 따라서 데이터는 다음과 같이 유지되어야 합니다.
 * ```
 * currentList = (list of GroupAlbumItem) + {Dummy GroupAlbumItem}
 * ```
 */
class GroupAlbumAdapter(
    private val parentViewModel: GroupAlbumListViewModel
): ListAdapter<GroupAlbumItem, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {
    override fun getItemViewType(position: Int): Int {
        return if(position == itemCount - 1) GroupAlbumViewType.CREATE.ordinal
        else GroupAlbumViewType.GROUP_ALBUM.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            GroupAlbumViewType.CREATE.ordinal -> {
                val binding = getViewDataBinding<ItemCreategroupalbumBinding>(parent, R.layout.item_creategroupalbum)
                val holder = CreateGroupAlbumViewHolder(binding)
                subscribeCreateGroupAlbumItemUi(binding, holder)

                holder
            }
            else -> {
                val binding = getViewDataBinding<ItemGroupalbumBinding>(parent, R.layout.item_groupalbum)
                val holder = GroupAlbumViewHolder(binding)
                subscribeGroupAlbumItemUi(binding, holder)

                holder
            }
        }
    }

    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T =
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)

    // TODO: 생성 플로우로 대체
    private fun subscribeCreateGroupAlbumItemUi(binding: ItemCreategroupalbumBinding, holder: CreateGroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if(!item.isCheckboxVisible){
                val count = itemCount-1
                parentViewModel.addGroupAlbumItem(GroupAlbumItem(
                    GroupAlbumDao(count.toLong(), "title$count", 100+count), false, false
                ))
            }
        }
    }

    private fun subscribeGroupAlbumItemUi(binding: ItemGroupalbumBinding, holder: GroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if(!item.isCheckboxVisible) {
                val directions = GroupAlbumParentViewPagerFragmentDirections
                        .actionGroupalbumparentviewpagerToGroupalbumviewpager(item.groupAlbumDao.id)
                binding.root.findNavController().navigate(directions)
            }else{
                binding.checkbox.isChecked = !binding.checkbox.isChecked
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
        (holder as ParentGroupAlbumViewHolder).bind(groupAlbumItem)
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