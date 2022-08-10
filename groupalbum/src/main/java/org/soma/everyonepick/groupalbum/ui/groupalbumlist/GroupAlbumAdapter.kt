package org.soma.everyonepick.groupalbum.ui.groupalbumlist

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
import org.soma.everyonepick.groupalbum.databinding.ItemCreateGroupAlbumBinding
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.ui.HomeViewPagerFragmentDirections
import org.soma.everyonepick.groupalbum.util.GroupAlbumViewType


/**
 * 가장 마지막 Item을 CreateGroupAlbumModel로 취급합니다.
 * 따라서 데이터는 [원본 데이터]*N + [더미 데이터] 형태를 유지해야 합니다.
 * @see GroupAlbumListViewModel
 */
class GroupAlbumAdapter(
    private val parentViewModel: GroupAlbumListViewModel
): ListAdapter<GroupAlbumModel, RecyclerView.ViewHolder>(GroupAlbumDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) GroupAlbumViewType.CREATE.ordinal
        else GroupAlbumViewType.GROUP_ALBUM.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            GroupAlbumViewType.CREATE.ordinal -> {
                val binding = getViewDataBinding<ItemCreateGroupAlbumBinding>(parent, R.layout.item_create_group_album)
                val holder = CreateGroupAlbumViewHolder(binding)
                subscribeCreateGroupAlbumModelUi(binding, holder)

                holder
            }
            else -> {
                val binding = getViewDataBinding<ItemGroupAlbumBinding>(parent, R.layout.item_group_album)
                val holder = GroupAlbumViewHolder(binding)
                subscribeGroupAlbumModelUi(binding, holder)

                holder
            }
        }
    }

    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T =
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)

    private fun subscribeCreateGroupAlbumModelUi(binding: ItemCreateGroupAlbumBinding, holder: CreateGroupAlbumViewHolder) {
        binding.cardview.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = HomeViewPagerFragmentDirections.toInvitationFragment()
                binding.root.findNavController().navigate(directions)
            }
        }
    }

    private fun subscribeGroupAlbumModelUi(binding: ItemGroupAlbumBinding, holder: GroupAlbumViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            // 일반 모드일 때
            if (!item.isCheckboxVisible) {
                val directions = HomeViewPagerFragmentDirections.toGroupAlbumFragment(item.groupAlbum.id)
                binding.root.findNavController().navigate(directions)
            } else {
                binding.checkbox.performTouch()
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.absoluteAdapterPosition
            parentViewModel.groupAlbumModelList.value?.data?.get(position)?.isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val groupAlbumModel = getItem(position)
        (holder as ParentGroupAlbumViewHolder).bind(groupAlbumModel)
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