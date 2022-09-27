package org.soma.everyonepick.groupalbum.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface Checkable {
    var isChecked: MutableStateFlow<Boolean>

    companion object {
        fun <T: Checkable> List<T>.toCheckedItemList() = this.filter { it.isChecked.value }
    }
}