package org.soma.everyonepick.groupalbum.adapter.groupalbum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemCreateGroupAlbumBinding
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding
import org.soma.everyonepick.groupalbum.ui.ViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.util.GroupAlbumViewType
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
        return if (position == itemCount - 1) GroupAlbumViewType.CREATE.ordinal
        else GroupAlbumViewType.GROUP_ALBUM.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            GroupAlbumViewType.CREATE.ordinal -> {
                val binding = getViewDataBinding<ItemCreateGroupAlbumBinding>(parent, R.layout.item_create_group_album)
                val holder = CreateGroupAlbumViewHolder(binding)
                subscribeCreateGroupAlbumItemUi(binding, holder)

                holder
            }
            else -> {
                val binding = getViewDataBinding<ItemGroupAlbumBinding>(parent, R.layout.item_group_album)
                val holder = GroupAlbumViewHolder(binding)
                subscribeGroupAlbumItemUi(binding, holder)

                holder
            }
        }
    }

    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T =
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)

    private fun subscribeCreateGroupAlbumItemUi(binding: ItemCreateGroupAlbumBinding, holder: CreateGroupAlbumViewHolder) {
        binding.cardview.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = ViewPagerFragmentDirections.toInvitationFragment()
                binding.root.findNavController().navigate(directions)
            }
        }
    }

    private fun subscribeGroupAlbumItemUi(binding: ItemGroupAlbumBinding, holder: GroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = ViewPagerFragmentDirections.toGroupAlbumViewPagerFragment(item.groupAlbum.id)
                binding.root.findNavController().navigate(directions)
            } else {
                binding.checkbox.performTouch()
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.absoluteAdapterPosition
            parentViewModel.groupAlbumItemList.value?.data?.get(position)?.isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupAlbumItem = getItem(position)
        (holder as ParentGroupAlbumViewHolder).bind(groupAlbumItem)
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbumItem>() {
    override fun areItemsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem.groupAlbum.id == newItem.groupAlbum.id
    }

    override fun areContentsTheSame(oldItem: GroupAlbumItem, newItem: GroupAlbumItem): Boolean {
        return oldItem == newItem
    }
}