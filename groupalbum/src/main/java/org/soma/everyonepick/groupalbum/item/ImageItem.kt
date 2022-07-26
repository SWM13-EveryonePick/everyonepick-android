package org.soma.everyonepick.groupalbum.item

import android.net.Uri

data class ImageItem(
    var uri: Uri,
    var isChecked: Boolean
)