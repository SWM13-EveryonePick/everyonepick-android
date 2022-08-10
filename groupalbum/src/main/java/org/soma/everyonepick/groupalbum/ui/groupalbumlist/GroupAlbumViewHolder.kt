package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import org.soma.everyonepick.common.util.setVisibility
import org.soma.everyonepick.groupalbum.databinding.ItemGroupAlbumBinding
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel

class GroupAlbumViewHolder(
    private val binding: ItemGroupAlbumBinding
): ParentGroupAlbumViewHolder(binding) {
    override fun bind(groupAlbumModel: GroupAlbumModel) {
        binding.textTitle.text = groupAlbumModel.groupAlbum.title
        binding.textPhotocount.text = "사진 ${groupAlbumModel.groupAlbum.photoCnt}장"
        binding.checkbox.setVisibility(groupAlbumModel.isCheckboxVisible)
        binding.checkbox.isChecked = groupAlbumModel.isChecked
    }
}