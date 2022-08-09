package org.soma.everyonepick.common.data.repository

import org.soma.everyonepick.common.api.RetrofitFactory.Companion.toBearerToken
import org.soma.everyonepick.common.api.UserService
import org.soma.everyonepick.common.data.item.MemberItem
import org.soma.everyonepick.common.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUser(token: String) = userService.getUser(token.toBearerToken())

    suspend fun getMemberList(groupAlbumId: Long): MutableList<MemberItem> {
        // TODO: userService.getUser(groupAlbumId)
        val memberList = mutableListOf(
            User(0, "차범근", "id", "https://picsum.photos/200", false),
            User(1, "박지성", "id", "https://picsum.photos/201", false),
            User(2, "설기현", "id", "https://picsum.photos/202", false),
            User(3, "리오넬", "id", "https://picsum.photos/203", false),
            User(4, "크리스티아누", "id", "https://picsum.photos/204", false)
        )
        return convertMemberListToMemberItemList(memberList)
    }

    private fun convertMemberListToMemberItemList(memberList: MutableList<User>): MutableList<MemberItem> {
        val result = mutableListOf<MemberItem>()
        memberList.forEach {
            result.add(MemberItem(it, isChecked = false, isCheckboxVisible = false))
        }
        return result
    }
}