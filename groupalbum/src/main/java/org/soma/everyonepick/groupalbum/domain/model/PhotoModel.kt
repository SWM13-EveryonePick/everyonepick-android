package org.soma.everyonepick.groupalbum.domain.model
import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.Photo
import org.soma.everyonepick.groupalbum.domain.Checkable

data class PhotoModel(
    val photo: Photo,
    override var isChecked: MutableStateFlow<Boolean>,
    override var isCheckboxVisible: Boolean
): Checkable {
    fun copy() = PhotoModel(photo.copy(), MutableStateFlow(isChecked.value), isCheckboxVisible)
}