package org.soma.everyonepick.groupalbum.domain.model
import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.data.entity.Photo

data class PhotoModel(
    val photo: Photo,
    var isChecked: MutableStateFlow<Boolean>,
    var isCheckboxVisible: Boolean
)