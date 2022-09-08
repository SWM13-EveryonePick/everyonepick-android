package org.soma.everyonepick.groupalbum.domain.usecase

import com.kakao.sdk.talk.model.Friend
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.groupAlbumReadListToGroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.toUserListWithClientId
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
) {
    suspend fun readGroupAlbumModelList(token: String): MutableList<GroupAlbumModel> {
        val groupAlbumList = groupAlbumRepository.readGroupAlbumList(token).data.toMutableList()
        return groupAlbumList.groupAlbumReadListToGroupAlbumModelList()
    }

    suspend fun readGroupAlbum(token: String, id: Long): GroupAlbum {
        return groupAlbumRepository.readGroupAlbum(token, id).data
    }

    suspend fun createGroupAlbum(token: String, groupAlbum: GroupAlbum): GroupAlbum {
        return groupAlbumRepository.createGroupAlbum(token, groupAlbum).data
    }

    suspend fun updateGroupAlbum(token: String, id: Long, groupAlbum: GroupAlbum): GroupAlbum {
        return groupAlbumRepository.updateGroupAlbum(token, id, groupAlbum).data
    }

    suspend fun inviteUsersToGroupAlbum(
        token: String, id: Long, friendListToInvite: MutableList<Friend>
    ): GroupAlbum {
        return groupAlbumRepository.inviteUsersToGroupAlbum(
            token, id, GroupAlbum(users = friendListToInvite.toUserListWithClientId())
        ).data
    }

    suspend fun kickUsersOutOfGroupAlbum(token: String, id: Long, userListToKick: MutableList<User>): GroupAlbum {
        return groupAlbumRepository.kickUsersOutOfGroupAlbum(
            token, id, GroupAlbum(users = userListToKick)
        ).data
    }

    suspend fun leaveGroupAlbum(token: String, id: Long): GroupAlbum {
        return groupAlbumRepository.leaveGroupAlbum(token, id).data
    }
}