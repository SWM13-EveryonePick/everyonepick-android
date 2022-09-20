package org.soma.everyonepick.groupalbum.domain.modellist

import android.util.Log
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import java.lang.Integer.max


/**
 * [data]의 마지막 아이템에 항상 [GroupAlbumModel.dummyData]가 위치하는 것을 보장하는 클래스입니다. [data]와는 달리,
 * 'Actual data'는 dummyData를 포함하지 않는 실제 데이터를 의미합니다.
 */
class GroupAlbumModelList {
    private var _data: MutableList<GroupAlbumModel>
    val data: List<GroupAlbumModel>
        get() = _data

    constructor(withDummyData: Boolean = true) {
        _data = mutableListOf()
        if (withDummyData) _data.add(GroupAlbumModel.createDummyData())
    }

    constructor(actualData: MutableList<GroupAlbumModel>, withDummyData: Boolean = true) {
        _data = actualData
        if (withDummyData) _data.add(GroupAlbumModel.createDummyData())
    }

    fun getActualItemCount() = max(data.size - 1, 0)

    fun getActualData() = data.subList(0, getActualItemCount()).toMutableList()

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in data.indices) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked.value = false
            _data[i] = newItem
        }
    }

    fun checkAll() {
        val isAllChecked = getActualData().all { it.isChecked.value }
        for (i in data.indices) {
            val newItem = copyGroupAlbumModel(data[i])
            newItem.isChecked.value = !isAllChecked
            _data[i] = newItem
        }
    }

    private fun copyGroupAlbumModel(groupAlbumModel: GroupAlbumModel) =
        GroupAlbumModel(groupAlbumModel.groupAlbum.copy(), groupAlbumModel.isChecked, groupAlbumModel.isCheckboxVisible)

    fun getNewInstance() = GroupAlbumModelList(_data, withDummyData = false)
}