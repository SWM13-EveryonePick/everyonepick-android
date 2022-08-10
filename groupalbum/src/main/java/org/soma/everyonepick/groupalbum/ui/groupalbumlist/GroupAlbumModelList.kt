package org.soma.everyonepick.groupalbum.ui.groupalbumlist

import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel


/**
 * data의 마지막 아이템에 항상 [GroupAlbumModel.dummyData]가 위치하는 것을 보장하는 클래스입니다.
 */
class GroupAlbumModelList {
    var data: MutableList<GroupAlbumModel>
        set(value) {
            field = value
            field.add(GroupAlbumModel.dummyData)
        }

    constructor() {
        data = mutableListOf(GroupAlbumModel.dummyData)
    }

    constructor(groupAlbumModelList: MutableList<GroupAlbumModel>) {
        data = groupAlbumModelList
    }

    fun getActualItemCount() = data.size - 1

    fun removeById(id: Long) {
        for (i in 0 until getActualItemCount()) {
            if (data[i].groupAlbum.id == id) {
                data.removeAt(i)
                break
            }
        }
    }

    fun removeCheckedItems() {
        val newData = mutableListOf<GroupAlbumModel>()
        for (i in 0 until getActualItemCount()) {
            if (!data[i].isChecked) newData.add(data[i])
        }
        data = newData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in 0 until data.size) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            data[i] = newItem
        }
    }

    fun checkAll() {
        val isAllChecked = data.subList(0, getActualItemCount()).all { it.isChecked }
        for (i in 0 until data.size) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isChecked = !isAllChecked
            data[i] = newItem
        }
    }

    private fun copyGroupAlbumModel(groupAlbumModel: GroupAlbumModel) =
        GroupAlbumModel(groupAlbumModel.groupAlbum.copy(), groupAlbumModel.isChecked, groupAlbumModel.isCheckboxVisible)
}