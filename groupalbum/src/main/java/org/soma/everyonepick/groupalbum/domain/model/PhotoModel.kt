package org.soma.everyonepick.groupalbum.domain.model
import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.Checkable

data class PhotoModel(
    val photo: Photo,
    override var isChecked: MutableStateFlow<Boolean>,
    var isCheckboxVisible: Boolean
): Checkable