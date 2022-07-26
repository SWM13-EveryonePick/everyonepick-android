package org.soma.everyonepick.groupalbum.data.item

import org.soma.everyonepick.groupalbum.data.model.PhotoDao

// Photo Recycler View에 들어가는 Item 객체입니다.
data class PhotoItem(
    val photoDao: PhotoDao,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
)