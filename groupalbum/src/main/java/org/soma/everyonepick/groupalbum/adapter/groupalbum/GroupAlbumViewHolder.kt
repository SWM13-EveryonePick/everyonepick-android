package org.soma.everyonepick.groupalbum.adapter.groupalbum

import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding

class GroupAlbumViewHolder(
    private val binding: ItemGroupAlbumBinding
): ParentGroupAlbumViewHolder(binding) {
    override fun bind(groupAlbumItem: GroupAlbumItem) {
        binding.textTitle.text = groupAlbumItem.groupAlbumDao.title
        binding.textPhotocount.text = "사진 ${groupAlbumItem.groupAlbumDao.photoCount}장"
        binding.checkbox.setVisibility(groupAlbumItem.isCheckboxVisible)
        binding.checkbox.isChecked = groupAlbumItem.isChecked
    }
}