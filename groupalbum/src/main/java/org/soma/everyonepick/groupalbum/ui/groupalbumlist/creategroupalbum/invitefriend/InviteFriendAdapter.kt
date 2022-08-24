package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.InviteFriendModel
import org.soma.everyonepick.groupalbum.databinding.ItemInviteFriendBinding

class InviteFriendAdapter(
    private val parentViewModel: InviteFriendViewModel
): ListAdapter<InviteFriendModel, RecyclerView.ViewHolder>(InviteFriendDiffCallback()) {
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
            val clickedItem = parentViewModel.filteredList.value[holder.absoluteAdapterPosition]
            val isChecked = binding.checkbox.isChecked
            parentViewModel.onClickCheckbox(clickedItem, isChecked)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend = getItem(position)
        (holder as InviteFriendViewHolder).bind(friend)
    }

    class InviteFriendViewHolder(
        private val binding: ItemInviteFriendBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(inviteFriendItem: InviteFriendModel) {
            Glide.with(binding.root)
                .load(inviteFriendItem.friend.profileThumbnailImage)
                .into(binding.imageProfile)
            binding.textNickname.text = inviteFriendItem.friend.profileNickname
            binding.checkbox.isChecked = inviteFriendItem.isChecked
        }
    }
}

private class InviteFriendDiffCallback: DiffUtil.ItemCallback<InviteFriendModel>() {
    override fun areItemsTheSame(oldItem: InviteFriendModel, newItem: InviteFriendModel): Boolean {
        return oldItem.friend.id == newItem.friend.id
    }

    override fun areContentsTheSame(oldItem: InviteFriendModel, newItem: InviteFriendModel): Boolean {
        return oldItem == newItem
    }
}