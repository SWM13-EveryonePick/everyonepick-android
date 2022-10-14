package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.groupalbum.domain.model.MemberModel
import java.lang.Integer.max

class MemberModelList: ListWithDummy<MemberModel> {
    private var _data: MutableList<MemberModel>
    override val data: List<MemberModel>
        get() = _data

    constructor() {
        _data = mutableListOf()
        _data.add(MemberModel.createDummyData())
    }

    constructor(actualData: MutableList<MemberModel>) {
        _data = actualData
        _data.add(MemberModel.createDummyData())
    }

    override fun getItemCountWithoutDummy() = max(data.size - 1, 0)

    override fun getListWithoutDummy() = data.subList(0, getItemCountWithoutDummy()).toMutableList()

    fun getNewInstance() = MemberModelList(getListWithoutDummy())
}