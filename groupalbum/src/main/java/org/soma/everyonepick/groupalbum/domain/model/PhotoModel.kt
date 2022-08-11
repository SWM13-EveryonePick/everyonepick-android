package org.soma.everyonepick.groupalbum.domain.model
import org.soma.everyonepick.groupalbum.data.entity.Photo

data class PhotoModel(
    val photo: Photo,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)