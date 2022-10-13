package org.soma.everyonepick.groupalbum.domain.translator

import org.soma.everyonepick.groupalbum.data.entity.PickInfo
import org.soma.everyonepick.groupalbum.domain.model.PickInfoModel

class PickInfoTranslator {
    companion object {
        fun PickInfo.toPickInfoModel(): PickInfoModel {
            return PickInfoModel(userCnt, pickUserCnt, timeout)
        }
    }
}