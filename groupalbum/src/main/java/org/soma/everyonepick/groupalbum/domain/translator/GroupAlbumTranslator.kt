package org.soma.everyonepick.groupalbum.domain.translator

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadList
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel

class GroupAlbumTranslator {
    companion object {
        fun MutableList<GroupAlbumReadList>.groupAlbumReadListToGroupAlbumModelList(): MutableList<GroupAlbumModel> {
            val groupAlbumModelList = mutableListOf<GroupAlbumModel>()
            for (i in 0 until size) {
                groupAlbumModelList.add(GroupAlbumModel(get(i), isChecked = MutableStateFlow(false), isCheckboxVisible = false))
            }
            return groupAlbumModelList
        }

        fun List<GroupAlbumLocal>.groupAlbumLocalListToGroupAlbumModelList(): MutableList<GroupAlbumModel> {
            val groupAlbumModelList = mutableListOf<GroupAlbumModel>()
            for (i in 0 until size) {
                val item = get(i)
                groupAlbumModelList.add(GroupAlbumModel(
                    GroupAlbumReadList(item.id, item.title, item.hostUserId, item.users, item.photoCnt),
                    isChecked = MutableStateFlow(false), isCheckboxVisible = false
                ))
            }
            return groupAlbumModelList
        }

        fun MutableList<GroupAlbumModel>.groupAlbumModelListToGroupAlbumLocalList(): List<GroupAlbumLocal> {
            val groupAlbumLocalList = mutableListOf<GroupAlbumLocal>()
            for (i in 0 until size) {
                val item = get(i).groupAlbum
                groupAlbumLocalList.add(GroupAlbumLocal(
                    i, item.id, item.title, item.hostUserId, item.users, item.photoCnt
                ))
            }
            return groupAlbumLocalList
        }
    }
}