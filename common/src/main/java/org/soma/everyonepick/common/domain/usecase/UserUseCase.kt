package org.soma.everyonepick.common.domain.usecase

import org.soma.everyonepick.common.data.repository.UserRepository
import org.soma.everyonepick.common.domain.model.MemberModel
import org.soma.everyonepick.common.domain.translator.UserTranslator.Companion.toMemberModelList
import org.soma.everyonepick.common.data.entity.User
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun getUser(token: String) = userRepository.getUser(token)

    fun getMemberList(groupAlbumId: Long): MutableList<MemberModel> {
        // TODO: userRepository.getUser(groupAlbumId)
        val memberList = mutableListOf(
            User(0, "차범근", "id", "https://picsum.photos/200", false),
            User(1, "박지성", "id", "https://picsum.photos/201", false),
            User(2, "설기현", "id", "https://picsum.photos/202", false),
            User(3, "리오넬", "id", "https://picsum.photos/203", false),
            User(4, "크리스티아누", "id", "https://picsum.photos/204", false)
        )
        return memberList.toMemberModelList()
    }
}