package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.common.domain.model.MemberModel

/**
 * @see GroupAlbumModelList
 */
class MemberModelList {
    private var _data: MutableList<MemberModel>
        set(value) {
            field = value
            field.add(MemberModel.createDummyData())
        }

    val data: List<MemberModel>
        get() = _data

    constructor() {
        _data = mutableListOf()
    }

    constructor(actualData: MutableList<MemberModel>) {
        _data = actualData
    }

    fun getActualItemCount() = _data.size - 1

    fun getActualData() = _data.subList(0, getActualItemCount()).toMutableList()

    fun setActualData(actualData: MutableList<MemberModel>) {
        _data = actualData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in _data.indices) {
            val newItem = copyMemberModel(_data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            _data[i] = newItem
        }
    }

    private fun copyMemberModel(memberItem: MemberModel) =
        MemberModel(memberItem.user.copy(), memberItem.isChecked, memberItem.isCheckboxVisible)

    fun getNewInstance() = MemberModelList(getActualData())
}