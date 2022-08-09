package org.soma.everyonepick.groupalbum.data.itemlist

import android.util.Log
import org.soma.everyonepick.common.data.item.MemberItem
import org.soma.everyonepick.groupalbum.data.item.GroupAlbumItem

/**
 * @see GroupAlbumItemList
 */
class MemberItemList {
    var data: MutableList<MemberItem>
        set(value) {
            field = value
            field.add(MemberItem.dummyData)
        }

    constructor() {
        data = mutableListOf(MemberItem.dummyData)
    }

    constructor(memberItemList: MutableList<MemberItem>) {
        data = memberItemList
    }

    fun getItemCount() = data.size - 1

    fun removeCheckedItems() {
        val newData = mutableListOf<MemberItem>()
        for (i in 0 until getItemCount()) {
            if (!data[i].isChecked) newData.add(data[i])
        }
        data = newData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in 0 until data.size) {
            val newItem = copyMemberItem(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            data[i] = newItem
        }
    }

    private fun copyMemberItem(memberItem: MemberItem) =
        MemberItem(memberItem.user.copy(), memberItem.isChecked, memberItem.isCheckboxVisible)
}