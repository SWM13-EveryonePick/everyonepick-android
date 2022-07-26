package org.soma.everyonepick.groupalbum.adapter.groupalbum

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem

/**
 * [GroupAlbumItem]의 뷰 타입이 2개로 나뉘며, 이에 따라 뷰홀더가 두 개로 나뉘게 됩니다.
 * 이 클래스는 두 개의 뷰홀더를 추상화하는 부모 클래스입니다.
 */
abstract class ParentGroupAlbumViewHolder(
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(groupAlbumItem: GroupAlbumItem)
}