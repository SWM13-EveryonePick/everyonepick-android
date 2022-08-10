package org.soma.everyonepick.common.domain.translator

import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.foundation.data.model.User

class UserTranslator {
    companion object {
        fun MutableList<User>.toMemberModelList(): MutableList<MemberModel> {
            val result = mutableListOf<MemberModel>()
            forEach {
                result.add(
                    MemberModel(
                        it,
                        isChecked = false,
                        isCheckboxVisible = false
                    )
                )
            }
            return result
        }
    }
}