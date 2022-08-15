package org.soma.everyonepick.groupalbum.domain.usecase

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetail
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.groupAlbumReadListToGroupAlbumModelList
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
) {
    suspend fun readGroupAlbumModelList(token: String): MutableList<GroupAlbumModel> {
        val groupAlbumList = groupAlbumRepository.readGroupAlbumList(token).data.toMutableList()
        return groupAlbumList.groupAlbumReadListToGroupAlbumModelList()
    }

    suspend fun readGroupAlbum(token: String, id: Long): GroupAlbumReadDetail {
        return groupAlbumRepository.readGroupAlbum(token, id).data
    }

    suspend fun createGroupAlbum(token: String, groupAlbum: GroupAlbum): GroupAlbumReadDetail {
        return groupAlbumRepository.createGroupAlbum(token, groupAlbum).data
    }

    suspend fun updateGroupAlbum(token: String, id: Long, groupAlbum: GroupAlbum): GroupAlbumReadDetail {
        return groupAlbumRepository.updateGroupAlbum(token, id, groupAlbum).data
    }

    suspend fun kickUsersOutOfGroupAlbum(token: String, id: Long, groupAlbum: GroupAlbum): GroupAlbumReadDetail {
        return groupAlbumRepository.kickUsersOutOfGroupAlbum(token, id, groupAlbum).data
    }

    suspend fun leaveGroupAlbum(token: String, id: Long): GroupAlbumReadDetail {
        return groupAlbumRepository.leaveGroupAlbum(token, id).data
    }
}