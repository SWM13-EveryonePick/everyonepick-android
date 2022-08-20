package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel


/**
 * [data]의 마지막 아이템에 항상 [GroupAlbumModel.dummyData]가 위치하는 것을 보장하는 클래스입니다. [data]와는 달리,
 * 'Actual data'는 dummyData를 포함하지 않는 실제 데이터를 의미합니다.
 */
class GroupAlbumModelList {
    private var _data: MutableList<GroupAlbumModel>
        set(value) {
            field = value
            field.add(GroupAlbumModel.dummyData)
        }

    val data: List<GroupAlbumModel>
        get() = _data

    constructor() {
        _data = mutableListOf()
    }

    constructor(actualData: MutableList<GroupAlbumModel>) {
        _data = actualData
    }

    fun getActualItemCount() = data.size - 1

    fun getActualData() = data.subList(0, getActualItemCount()).toMutableList()

    fun setActualData(actualData: MutableList<GroupAlbumModel>) {
        _data = actualData
    }

    fun removeCheckedItems() {
        val newData = mutableListOf<GroupAlbumModel>()
        for (i in 0 until getActualItemCount()) {
            if (!data[i].isChecked) newData.add(data[i])
        }
        _data = newData
    }

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in data.indices) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked = false
            _data[i] = newItem
        }
    }

    fun checkAll() {
        val isAllChecked = getActualData().all { it.isChecked }
        for (i in data.indices) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isChecked = !isAllChecked
            _data[i] = newItem
        }
    }

    fun getNewInstance() = GroupAlbumModelList(getActualData())

    private fun copyGroupAlbumModel(groupAlbumModel: GroupAlbumModel) =
        GroupAlbumModel(groupAlbumModel.groupAlbum.copy(), groupAlbumModel.isChecked, groupAlbumModel.isCheckboxVisible)
}