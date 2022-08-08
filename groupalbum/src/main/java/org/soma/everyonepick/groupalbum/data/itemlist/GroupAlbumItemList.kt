package org.soma.everyonepick.groupalbum.data.itemlist

import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem

/**
 * data의 마지막 아이템에 항상 [GroupAlbumItem.dummyData]가 위치하는 것을 보장하는 클래스입니다.
 */
class GroupAlbumItemList {
    var data: MutableList<GroupAlbumItem>
        set(value) {
            field = value
            field.add(GroupAlbumItem.dummyData)
        }

    constructor() {
        data = mutableListOf(GroupAlbumItem.dummyData)
    }

    constructor(groupAlbumItemList: MutableList<GroupAlbumItem>) {
        data = groupAlbumItemList
    }

    fun getItemCount() = data.size - 1

    fun removeById(id: Long) {
        for (i in 0 until getItemCount()) {
            if (data[i].groupAlbum.id == id) {
                data.removeAt(i)
                break
            }
        }
    }

    fun removeCheckedItems() {
        val newData = mutableListOf<GroupAlbumItem>()
        for (i in 0 until getItemCount()) {
            if (!data[i].isChecked) newData.add(data[i])
        }
        data = newData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in 0 until data.size) {
            val newItem = copyGroupAlbumItem(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            data[i] = newItem
        }
    }

    fun checkAll() {
        val isAllChecked = data.subList(0, getItemCount()).all { it.isChecked }
        for (i in 0 until data.size) {
            val newItem = copyGroupAlbumItem(data[i])
            newItem.isChecked = !isAllChecked
            data[i] = newItem
        }
    }

    private fun copyGroupAlbumItem(groupAlbumItem: GroupAlbumItem) =
        GroupAlbumItem(groupAlbumItem.groupAlbum.copy(), groupAlbumItem.isChecked, groupAlbumItem.isCheckboxVisible)
}