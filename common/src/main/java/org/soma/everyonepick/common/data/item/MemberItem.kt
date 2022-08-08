package org.soma.everyonepick.common.data.item

import org.soma.everyonepick.common.data.model.User

/**
 * 단체공유앨범에 속하는 [User]를 지칭합니다.
 */
class MemberItem(
    val user: User,
    var isChecked: Boolean,
    var isCheckboxVisible: Boolean
) {
    companion object {
        val dummyData = MemberItem(
            User(-1, "TEST", "", "", isPushActive = false),
            isChecked = false,
            isCheckboxVisible = false
        )
    }

    /**
     * @see GroupAlbumItem
     */
    override fun equals(other: Any?) = super.equals(other)
}