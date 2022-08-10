package org.soma.everyonepick.groupalbum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.common.util.performTouch
import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.adapter.groupalbum.GroupAlbumAdapter
import org.soma.everyonepick.groupalbum.databinding.ItemMemberBinding
import org.soma.everyonepick.groupalbum.util.MemberViewType
import org.soma.everyonepick.groupalbum.util.SelectionMode
import org.soma.everyonepick.groupalbum.viewmodel.GroupAlbumViewPagerViewModel

/**
 * @see GroupAlbumAdapter
 */
class MemberAdapter(
    private val parentViewModel: GroupAlbumViewPagerViewModel
): ListAdapter<MemberModel, RecyclerView.ViewHolder>(MemberDiffCall()) {

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) MemberViewType.INVITE.ordinal
        else MemberViewType.MEMBER.ordinal
    }

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
            val isInviteItem = holder.absoluteAdapterPosition == itemCount - 1
            if (isInviteItem) {
                // TODO: 초대하기
            } else {
                if (binding.checkbox.visibility == View.VISIBLE) {
                    binding.checkbox.performTouch()
                }
            }
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val position = holder.absoluteAdapterPosition
            parentViewModel.memberItemList.value?.data?.get(position)?.isChecked = isChecked
            parentViewModel.checked.value =
                if (isChecked) parentViewModel.checked.value?.plus(1)
                else parentViewModel.checked.value?.minus(1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val memberItem = getItem(position)
        (holder as MemberViewHolder).bind(
            memberItem,
            isInviteItem = position == itemCount - 1,
            parentViewModel
        )
    }

    class MemberViewHolder(
        private val binding: ItemMemberBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            memberItem: MemberModel,
            isInviteItem: Boolean,
            parentViewModel: GroupAlbumViewPagerViewModel
        ) {
            if (isInviteItem) {
                binding.root.setVisibility(parentViewModel.memberSelectionMode.value == SelectionMode.NORMAL_MODE.ordinal)

                Glide.with(binding.root)
                    .load(R.drawable.ic_btn_drawer_add)
                    .into(binding.imageProfile)

                binding.textNickname.text = "초대하기"
                binding.textNickname.setTextColor(
                    ContextCompat.getColor(binding.root.context, org.soma.everyonepick.common_ui.R.color.primary_blue)
                )

                binding.checkbox.visibility = View.GONE
                binding.imageCrown.visibility = View.GONE
            } else {
                Glide.with(binding.root)
                    .load(memberItem.user.thumbnailImageUrl)
                    .into(binding.imageProfile)

                binding.textNickname.text = memberItem.user.nickname
                binding.checkbox.isChecked = memberItem.isChecked

                val isHostUser = parentViewModel.groupAlbum.value?.hostUserId == memberItem.user.id
                binding.imageCrown.setVisibility(isHostUser)
                // 방장일 경우 체크박스를 표시하지 않습니다.
                binding.checkbox.setVisibility(memberItem.isCheckboxVisible && !isHostUser)
            }
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