package org.soma.everyonepick.groupalbum.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.common.domain.Checkable
import org.soma.everyonepick.groupalbum.data.entity.ResultPhoto

data class ResultPhotoModel(
    val resultPhoto: ResultPhoto,
    override var isChecked: MutableStateFlow<Boolean>,
    override var isCheckboxVisible: Boolean
): Checkable {
    fun copy() = ResultPhotoModel(resultPhoto.copy(), MutableStateFlow(isChecked.value), isCheckboxVisible)
}