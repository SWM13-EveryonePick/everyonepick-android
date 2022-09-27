package org.soma.everyonepick.groupalbum.domain.model

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.groupalbum.domain.Checkable

data class ImageModel(
    var uri: Uri,
    override var isChecked: MutableStateFlow<Boolean>,
    override var isCheckboxVisible: Boolean = true
): Checkable