package org.soma.everyonepick.groupalbum.domain.usecase

import org.soma.everyonepick.common.data.RetrofitFactory.Companion.toBearerToken
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumModelList
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumRepository: GroupAlbumRepository
) {
    suspend fun getGroupAlbumModelList(token: String): MutableList<GroupAlbumModel> {
        val groupAlbumList = groupAlbumRepository.getGroupAlbumList(token).data.toMutableList()
        return groupAlbumList.toGroupAlbumModelList()
    }

    fun getGroupAlbum(id: Long): GroupAlbum {
        // TODO: Retrofit2... req: id -> res: GroupAlbum
        return GroupAlbum(
            id, "title$id", id, listOf(), 100+id.toInt()
        )
    }
}