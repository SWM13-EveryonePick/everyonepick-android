package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.InviteFriendModel
import org.soma.everyonepick.groupalbum.databinding.ItemInviteFriendBinding

class InviteFriendAdapter: ListAdapter<InviteFriendModel, RecyclerView.ViewHolder>(InviteFriendDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemInviteFriendBinding>(parent, R.layout.item_invite_friend).apply {
            onClickRoot = View.OnClickListener {
                checkbox.performTouch()
            }
        }
        return InviteFriendViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend = getItem(position)
        (holder as InviteFriendViewHolder).bind(friend)
    }

    class InviteFriendViewHolder(private val binding: ItemInviteFriendBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(inviteFriendItem: InviteFriendModel) {
            binding.inviteFriendModel = inviteFriendItem
            binding.executePendingBindings()
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