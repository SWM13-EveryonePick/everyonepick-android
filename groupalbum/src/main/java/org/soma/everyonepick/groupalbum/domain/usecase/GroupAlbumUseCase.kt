package org.soma.everyonepick.groupalbum.domain.usecase

import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumDto
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumReadDetailDto
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumModelList
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
) {
    suspend fun readGroupAlbumModelList(token: String): MutableList<GroupAlbumModel> {
        val groupAlbumList = groupAlbumRepository.readGroupAlbumList(token).data.toMutableList()
        return groupAlbumList.toGroupAlbumModelList()
    }

    suspend fun readGroupAlbum(token: String, id: Long): GroupAlbumReadDetailDto {
        return groupAlbumRepository.readGroupAlbum(token, id).data
    }

    suspend fun createGroupAlbum(token: String, groupAlbumDto: GroupAlbumDto): GroupAlbumReadDetailDto {
        return groupAlbumRepository.createGroupAlbum(token, groupAlbumDto).data
    }

    suspend fun updateGroupAlbum(token: String, id: Long, groupAlbumDto: GroupAlbumDto): GroupAlbumReadDetailDto {
        return groupAlbumRepository.updateGroupAlbum(token, id, groupAlbumDto).data
    }
}