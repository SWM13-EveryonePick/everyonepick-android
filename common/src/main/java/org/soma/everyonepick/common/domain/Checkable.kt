package org.soma.everyonepick.common.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface Checkable {
    var isChecked: MutableStateFlow<Boolean>
    var isCheckboxVisible: Boolean

    companion object {
        fun <T: Checkable> List<T>.toCheckedItemList() = this.filter { it.isChecked.value }
        fun <T: Checkable> List<T>.setIsCheckboxVisible(flag: Boolean) {
            for (i in this.indices) {
                this[i].isChecked.value = false
                this[i].isCheckboxVisible = flag
            }
        }
        fun <T: Checkable> List<T>.checkAll() {
            val isAllChecked = all { it.isChecked.value }
            for (i in this.indices) {
                this[i].isChecked.value = !isAllChecked
                this[i].isCheckboxVisible = this[i].isCheckboxVisible
            }
        }
    }
}