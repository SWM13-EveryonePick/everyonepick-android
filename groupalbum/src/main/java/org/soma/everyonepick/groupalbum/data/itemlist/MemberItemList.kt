package org.soma.everyonepick.groupalbum.data.itemlist

import org.soma.everyonepick.common.data.item.MemberItem

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
}