package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemCreateGroupAlbumBinding
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragmentType
import org.soma.everyonepick.groupalbum.util.GroupAlbumViewType


/**
 * 가장 마지막 Item을 CreateGroupAlbumModel로 취급합니다.
 * 따라서 데이터는 [원본 데이터]*N + [더미 데이터] 형태를 유지해야 합니다.
 * @see GroupAlbumListViewModel
 */
class GroupAlbumAdapter: ListAdapter<GroupAlbumModel, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) GroupAlbumViewType.CREATE.ordinal
        else GroupAlbumViewType.GROUP_ALBUM.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GroupAlbumViewType.CREATE.ordinal -> {
                val binding = getViewDataBinding<ItemCreateGroupAlbumBinding>(parent, R.layout.item_create_group_album)
                val holder = CreateGroupAlbumViewHolder(binding)

                binding.onClickCardView = View.OnClickListener {
                    val item = getItem(holder.absoluteAdapterPosition)
                    // 일반 모드일 때
                    if (!item.isCheckboxVisible) {
                        val directions = HomeViewPagerFragmentDirections.toInviteFriendFragment(InviteFriendFragmentType.TO_CREATE)
                        binding.root.findNavController().navigate(directions)
                    }
                }

                holder
            }
            else -> {
                val binding = getViewDataBinding<ItemGroupAlbumBinding>(parent, R.layout.item_group_album)
                val holder = GroupAlbumViewHolder(binding)

                binding.onClickRoot = View.OnClickListener {
                    val item = getItem(holder.absoluteAdapterPosition)
                    // 일반 모드일 때
                    if (!item.isCheckboxVisible) {
                        val directions = HomeViewPagerFragmentDirections.toGroupAlbumFragment(item.groupAlbum.id?:0)
                        binding.root.findNavController().navigate(directions)
                    } else {
                        binding.checkbox.performTouch()
                    }
                }

                holder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupAlbumViewHolder) {
            val groupAlbumModel = getItem(position)
            holder.bind(groupAlbumModel)
        }
    }

    class CreateGroupAlbumViewHolder(binding: ItemCreateGroupAlbumBinding): RecyclerView.ViewHolder(binding.root)

    class GroupAlbumViewHolder(private val binding: ItemGroupAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(groupAlbumModel: GroupAlbumModel) {
            binding.groupAlbumModel = groupAlbumModel
            binding.executePendingBindings()
        }
    }
}

private class GroupAlbumDiffCallback: DiffUtil.ItemCallback<GroupAlbumModel>() {
    override fun areItemsTheSame(oldItem: GroupAlbumModel, newItem: GroupAlbumModel): Boolean {
        return oldItem.groupAlbum.id == newItem.groupAlbum.id
    }

    override fun areContentsTheSame(oldItem: GroupAlbumModel, newItem: GroupAlbumModel): Boolean {
        return oldItem == newItem
    }
}