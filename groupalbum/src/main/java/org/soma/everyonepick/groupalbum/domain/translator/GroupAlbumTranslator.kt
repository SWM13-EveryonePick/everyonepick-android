package org.soma.everyonepick.groupalbum.domain.translator

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel

class GroupAlbumTranslator {
    companion object {
        fun MutableList<GroupAlbum>.toGroupAlbumModelList(): MutableList<GroupAlbumModel> {
            val groupAlbumModelList = mutableListOf<GroupAlbumModel>()
            for(i in 0 until size) {
                groupAlbumModelList.add(GroupAlbumModel(get(i), isChecked = false, isCheckboxVisible = false))
            }
            return groupAlbumModelList
        }
    }
}