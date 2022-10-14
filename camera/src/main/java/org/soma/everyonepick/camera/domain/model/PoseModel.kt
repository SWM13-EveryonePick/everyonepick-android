package org.soma.everyonepick.camera.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import org.soma.everyonepick.camera.data.entity.Pose
import org.soma.everyonepick.common.domain.Checkable

data class PoseModel(
    val pose: Pose,
    override var isChecked: MutableStateFlow<Boolean>,
    override var isCheckboxVisible: Boolean
): Checkable
