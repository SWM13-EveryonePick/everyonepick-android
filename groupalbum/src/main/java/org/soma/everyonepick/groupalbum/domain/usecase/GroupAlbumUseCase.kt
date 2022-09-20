package org.soma.everyonepick.groupalbum.domain.usecase

import com.kakao.sdk.talk.model.Friend
import okhttp3.MultipartBody
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.data.dto.PhotoIdListRequest
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumService
import org.soma.everyonepick.groupalbum.domain.model.GroupAlbumModel
import org.soma.everyonepick.groupalbum.domain.model.PhotoModel
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.PhotoTranslator.Companion.toPhotoModelList
import org.soma.everyonepick.groupalbum.domain.translator.toUserListWithClientId
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumService: GroupAlbumService
) {
    suspend fun readGroupAlbumModelList(token: String): MutableList<GroupAlbumModel> {
        val groupAlbumList = groupAlbumService.readGroupAlbumList(token).data.toMutableList()
        return groupAlbumList.toGroupAlbumModelList()
    }

    suspend fun readGroupAlbum(token: String, id: Long): GroupAlbum {
        return groupAlbumService.readGroupAlbum(token, id).data
    }

    suspend fun createGroupAlbum(token: String, groupAlbum: GroupAlbum): GroupAlbum {
        return groupAlbumService.createGroupAlbum(token, groupAlbum).data
    }

    suspend fun updateGroupAlbum(token: String, id: Long, groupAlbum: GroupAlbum): GroupAlbum {
        return groupAlbumService.updateGroupAlbum(token, id, groupAlbum).data
    }

    suspend fun inviteUsersToGroupAlbum(
        token: String, id: Long, friendListToInvite: MutableList<Friend>
    ): GroupAlbum {
        return groupAlbumService.inviteUsersToGroupAlbum(
            token, id, GroupAlbum(users = friendListToInvite.toUserListWithClientId())
        ).data
    }

    suspend fun kickUsersOutOfGroupAlbum(token: String, id: Long, userListToKick: MutableList<User>): GroupAlbum {
        return groupAlbumService.kickUsersOutOfGroupAlbum(
            token, id, GroupAlbum(users = userListToKick)
        ).data
    }

    suspend fun leaveGroupAlbum(token: String, id: Long): GroupAlbum {
        return groupAlbumService.leaveGroupAlbum(token, id).data
    }

    suspend fun readPhotoList(token: String, id: Long): MutableList<PhotoModel> {
        val photoList = groupAlbumService.readPhotoList(token, id).data.toMutableList()
        return photoList.toPhotoModelList()
    }

    suspend fun createPhotoList(
        token: String,
        id: Long,
        images: List<MultipartBody.Part>
    ): MutableList<PhotoModel> {
        val photoList = groupAlbumService.createPhotoList(token, id, images).data.toMutableList()
        return photoList.toPhotoModelList()
    }

    suspend fun deletePhotoList(
        token: String,
        id: Long,
        photoIdList: PhotoIdListRequest
    ) {
        groupAlbumService.deletePhotoList(token, id, photoIdList)
    }
}