package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.common.data.item.MemberItem
import org.soma.everyonepick.groupalbum.databinding.ItemMemberBinding
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel

class MemberAdapter(
    private val parentViewModel: GroupAlbumViewPagerViewModel
): ListAdapter<MemberItem, RecyclerView.ViewHolder>(MemberDiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMemberBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_member,
            parent,
            false
        )

        val holder = MemberViewHolder(binding)
        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemMemberBinding, holder: MemberViewHolder) {
        binding.root.setOnClickListener {
            val item = getItem(holder.absoluteAdapterPosition)
            if (item.isCheckboxVisible) {
                binding.checkbox.performTouch()
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.absoluteAdapterPosition
            parentViewModel.memberItemList.value?.data?.get(position)?.isChecked = isChecked
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val memberItem = getItem(position)
        (holder as MemberViewHolder).bind(memberItem)
    }

    class MemberViewHolder(
        private val binding: ItemMemberBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(memberItem: MemberItem) {
            Glide.with(binding.root)
                .load(memberItem.user.thumbnailImageUrl)
                .into(binding.imageProfile)
            binding.textNickname.text = memberItem.user.nickname
            binding.checkbox.setVisibility(memberItem.isCheckboxVisible)
            binding.checkbox.isChecked = memberItem.isChecked
        }
    }
}

private class MemberDiffCall: DiffUtil.ItemCallback<MemberItem>() {
    override fun areItemsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean {
        return oldItem.user.id == newItem.user.id
    }

    override fun areContentsTheSame(oldItem: MemberItem, newItem: MemberItem): Boolean {
        return oldItem == newItem
    }
}