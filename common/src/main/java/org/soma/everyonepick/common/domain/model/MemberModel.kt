package org.soma.everyonepick.common.domain.model

import org.soma.everyonepick.common.data.entity.User

/**
 * 단체공유앨범에 속하는 [User]를 지칭합니다.
 */
class MemberModel(
    val user: User,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        val dummyData = MemberModel(
            User.dummyData,
            isChecked = false,
            isCheckboxVisible = false
        )
    }

    // DiffCall의 areContentsTheSame()에서의 오류를 해결하기 위함
    override fun equals(other: Any?) = super.equals(other)
}