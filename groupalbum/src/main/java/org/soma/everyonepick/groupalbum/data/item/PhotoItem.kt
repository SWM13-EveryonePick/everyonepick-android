package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.PhotoDao

data class PhotoItem(
    val photoDao: PhotoDao,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)