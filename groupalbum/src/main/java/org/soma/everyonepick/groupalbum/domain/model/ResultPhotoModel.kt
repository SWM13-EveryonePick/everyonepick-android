package org.soma.everyonepick.groupalbum.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.ResultPhoto
import org.soma.everyonepick.groupalbum.domain.Checkable

data class ResultPhotoModel(
    val resultPhoto: ResultPhoto,
    override var isChecked: MutableStateFlow<Boolean>,
    override var isCheckboxVisible: Boolean
): Checkable