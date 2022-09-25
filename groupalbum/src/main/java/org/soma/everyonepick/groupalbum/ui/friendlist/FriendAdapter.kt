package org.soma.everyonepick.groupalbum.ui.friendlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.talk.model.Friend
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemFriendBinding

class FriendAdapter: ListAdapter<Friend, RecyclerView.ViewHolder>(FriendDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = getViewDataBinding<ItemFriendBinding>(parent, R.layout.item_friend)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend = getItem(position)
        (holder as FriendViewHolder).bind(friend)
    }

    class FriendViewHolder(private val binding: ItemFriendBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.friend = friend
            binding.executePendingBindings()
        }
    }
}

private class FriendDiffCallback: DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem == newItem
    }
}