package org.soma.everyonepick.groupalbum.viewholder

import android.view.View
import org.soma.everyonepick.groupalbum.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding

class GroupAlbumViewHolder(
    private val binding: ItemGroupAlbumBinding
): ParentGroupAlbumViewHolder(binding) {
    override fun bind(groupAlbumItem: GroupAlbumItem) {
        binding.textTitle.text = groupAlbumItem.groupAlbumDao.title
        binding.textPhotocount.text = "사진 ${groupAlbumItem.groupAlbumDao.photoCount}장"
        binding.checkbox.visibility = if(groupAlbumItem.isCheckboxVisible) View.VISIBLE else View.GONE
        binding.checkbox.isChecked = groupAlbumItem.isChecked
    }
}