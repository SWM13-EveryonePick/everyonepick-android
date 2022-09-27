package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import java.lang.Integer.max

class GroupAlbumModelList: ListWithDummy<GroupAlbumModel> {
    private var _data: MutableList<GroupAlbumModel>
    override val data: List<GroupAlbumModel>
        get() = _data

    constructor(withDummyData: Boolean = true) {
        _data = mutableListOf()
        if (withDummyData) _data.add(GroupAlbumModel.createDummyData())
    }

    constructor(actualData: MutableList<GroupAlbumModel>, withDummyData: Boolean = true) {
        _data = actualData
        if (withDummyData) _data.add(GroupAlbumModel.createDummyData())
    }

    override fun getItemCountWithoutDummy() = max(data.size - 1, 0)

    override fun getListWithoutDummy() = data.subList(0, getItemCountWithoutDummy()).toMutableList()

    fun getNewInstance() = GroupAlbumModelList(_data, withDummyData = false)
}