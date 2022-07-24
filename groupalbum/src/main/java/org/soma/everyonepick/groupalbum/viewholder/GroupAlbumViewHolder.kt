package org.soma.everyonepick.groupalbum.viewholder

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.soma.everyonepick.groupalbum.data.GroupAlbumItem
import org.soma.everyonepick.groupalbum.databinding.ItemGroupalbumBinding
import org.soma.everyonepick.groupalbum.ui.GroupAlbumParentViewPagerFragmentDirections

class GroupAlbumViewHolder(
    private val binding: ItemGroupalbumBinding
): ParentGroupAlbumViewHolder(binding) {
    override fun bind(groupAlbumItem: GroupAlbumItem) {
        binding.textTitle.text = groupAlbumItem.groupAlbumDao.title
        binding.textPhotocount.text = "사진 ${groupAlbumItem.groupAlbumDao.photoCount}장"
        binding.checkbox.visibility = if(groupAlbumItem.isCheckboxVisible) View.VISIBLE else View.GONE
        binding.checkbox.isChecked = groupAlbumItem.isChecked
    }
}