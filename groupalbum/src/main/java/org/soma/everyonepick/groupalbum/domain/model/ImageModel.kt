package org.soma.everyonepick.groupalbum.domain.model

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow

data class ImageModel(
    var uri: Uri,
    var isChecked: MutableStateFlow<Boolean>
)