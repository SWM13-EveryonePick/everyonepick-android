package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum

import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.common_ui.util.BindingUtil.Companion.getViewDataBinding
import org.soma.everyonepick.common_ui.util.performTouch
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.databinding.ItemInviteMemberBinding
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.ItemMemberBinding
import org.soma.everyonepick.groupalbum.domain.modellist.MemberModelList
import org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.invitefriend.InviteFriendFragmentType
import org.soma.everyonepick.groupalbum.util.MemberViewType
import org.soma.everyonepick.groupalbum.util.SelectionMode

/**
 * @see GroupAlbumAdapter
 * @see MemberModelList
 */
class MemberAdapter(
    private val parentViewModel: GroupAlbumViewModel
): ListAdapter<MemberModel, RecyclerView.ViewHolder>(MemberDiffCall()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) MemberViewType.INVITE.ordinal
        else MemberViewType.MEMBER.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MemberViewType.INVITE.ordinal -> {
                val binding = getViewDataBinding<ItemInviteMemberBinding>(parent, R.layout.item_invite_member).apply {
                    onClickRoot = View.OnClickListener {
                        // 초대 페이지로 이동
                        val existingUserClientIdList = parentViewModel.memberModelList.value.getListWithoutDummy()
                            .filter { it.user.id != parentViewModel.me.value.id }
                            .map { it.user.clientId?: "" }
                            .toTypedArray()
                        val directions = GroupAlbumFragmentDirections.toInviteFriendFragment(
                            InviteFriendFragmentType.TO_INVITE,
                            existingUserClientIdList
                        )
                        root.findNavController().navigate(directions)
                    }
                }
                InviteMemberViewHolder(binding)
            }
            else -> {
                val binding = getViewDataBinding<ItemMemberBinding>(parent, R.layout.item_member).apply {
                    onClickRoot = View.OnClickListener {
                        if (checkbox.visibility == View.VISIBLE) {
                            checkbox.performTouch()
                        }
                    }
                }
                MemberViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val memberModel = getItem(position)
        when (holder) {
            is MemberViewHolder -> holder.bind(memberModel, parentViewModel)
            is InviteMemberViewHolder -> holder.bind(memberModel, parentViewModel)
        }
    }

    class MemberViewHolder(private val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            memberModel: MemberModel,
            parentViewModel: GroupAlbumViewModel
        ) {
            binding.memberModel = memberModel
            binding.isMemberHostUser = memberModel.user.id == parentViewModel.groupAlbum.value.hostUserId
            binding.executePendingBindings()
        }
    }

    class InviteMemberViewHolder(private val binding: ItemInviteMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            memberModel: MemberModel,
            parentViewModel: GroupAlbumViewModel
        ) {
            binding.memberModel = memberModel
            val amIHost = parentViewModel.groupAlbum.value.hostUserId == parentViewModel.me.value.id
            val isModeNormal = parentViewModel.memberSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal
            binding.visibility = amIHost && isModeNormal
            binding.executePendingBindings()
        }
    }
}

private class MemberDiffCall: DiffUtil.ItemCallback<MemberModel>() {
    override fun areItemsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
        return oldItem.user.id == newItem.user.id
    }

    override fun areContentsTheSame(oldItem: MemberModel, newItem: MemberModel): Boolean {
        return oldItem == newItem
    }
}