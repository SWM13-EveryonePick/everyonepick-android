package org.soma.everyonepick.groupalbum.domain.modellist

import org.soma.everyonepick.common.domain.model.MemberModel
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

    fun setIsCheckboxVisible(isCheckboxVisible: Boolean) {
        for (i in _data.indices) {
            val newItem = copyMemberModel(_data[i])
            newItem.isCheckboxVisible = isCheckboxVisible
            newItem.isChecked.value = false
            _data[i] = newItem
        }
    }

    private fun copyMemberModel(memberItem: MemberModel) =
        MemberModel(memberItem.user.copy(), memberItem.isChecked, memberItem.isCheckboxVisible)

    fun getNewInstance() = MemberModelList(getListWithoutDummy())
}