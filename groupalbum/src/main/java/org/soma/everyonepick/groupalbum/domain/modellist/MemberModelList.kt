package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.common.domain.model.MemberModel

/**
 * @see GroupAlbumModelList
 */
class MemberModelList {
    var data: MutableList<MemberModel>
        set(value) {
            field = value
            field.add(MemberModel.dummyData)
        }

    constructor() {
        data = mutableListOf(MemberModel.dummyData)
    }

    constructor(memberModelList: MutableList<MemberModel>) {
        data = memberModelList
    }

    fun getActualItemCount() = data.size - 1

    fun removeCheckedItems() {
        val newData = mutableListOf<MemberModel>()
        for (i in 0 until getActualItemCount()) {
            if (!data[i].isChecked) newData.add(data[i])
        }
        data = newData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in 0 until data.size) {
            val newItem = copyMemberModel(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            data[i] = newItem
        }
    }

    private fun copyMemberModel(memberItem: MemberModel) =
        MemberModel(memberItem.user.copy(), memberItem.isChecked, memberItem.isCheckboxVisible)
}