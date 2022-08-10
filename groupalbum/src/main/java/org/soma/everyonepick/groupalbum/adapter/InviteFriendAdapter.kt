package org.soma.everyonepick.groupalbum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.sdk.talk.model.Friend
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.item.InviteFriendItem
import org.soma.everyonepick.groupalbum.databinding.ItemInviteFriendBinding
import org.soma.everyonepick.groupalbum.viewmodel.InviteFriendViewModel

class InviteFriendAdapter(
    private val parentViewModel: InviteFriendViewModel
): ListAdapter<InviteFriendItem, RecyclerView.ViewHolder>(InviteFriendDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemInviteFriendBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_invite_friend,
            parent,
            false
        )
        val holder = InviteFriendViewHolder(binding)
        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemInviteFriendBinding, holder: InviteFriendViewHolder) {
        binding.root.setOnClickListener {
            binding.checkbox.performTouch()
        }

        binding.checkbox.setOnClickListener {
            onClickCheckbox(binding, holder)
        }
    }

    private fun onClickCheckbox(binding: ItemInviteFriendBinding, holder: InviteFriendViewHolder) {
        val inviteFriendItemList = parentViewModel.inviteFriendItemList.value?: return
        val filteredList = parentViewModel.filteredList.value?: return
        val isChecked = binding.checkbox.isChecked

        val itemAtFilteredList = filteredList[holder.absoluteAdapterPosition]
        val itemAtInviteFriendItemList = inviteFriendItemList.find { it.friend.id == itemAtFilteredList.friend.id }
        itemAtInviteFriendItemList?.isChecked = isChecked

        parentViewModel.checked.value =
            if (isChecked) parentViewModel.checked.value?.plus(1)
            else parentViewModel.checked.value?.minus(1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend = getItem(position)
        (holder as InviteFriendViewHolder).bind(friend)
    }

    class InviteFriendViewHolder(
        private val binding: ItemInviteFriendBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(inviteFriendItem: InviteFriendItem) {
            Glide.with(binding.root)
                .load(inviteFriendItem.friend.profileThumbnailImage)
                .into(binding.imageProfile)
            binding.textNickname.text = inviteFriendItem.friend.profileNickname
            binding.checkbox.isChecked = inviteFriendItem.isChecked
        }
    }
}

private class InviteFriendDiffCallback: DiffUtil.ItemCallback<InviteFriendItem>() {
    override fun areItemsTheSame(oldItem: InviteFriendItem, newItem: InviteFriendItem): Boolean {
        return oldItem.friend.id == newItem.friend.id
    }

    override fun areContentsTheSame(oldItem: InviteFriendItem, newItem: InviteFriendItem): Boolean {
        return oldItem == newItem
    }
}