package org.soma.everyonepick.groupalbum.domain.model

import android.net.Uri

data class ImageModel(
    var uri: Uri,
    var isChecked: Boolean
)