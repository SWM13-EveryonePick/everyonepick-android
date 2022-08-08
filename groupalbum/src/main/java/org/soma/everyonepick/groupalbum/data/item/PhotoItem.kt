package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.Photo

data class PhotoItem(
    val photo: Photo,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)