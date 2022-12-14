package org.soma.everyonepick.groupalbum.domain.usecase

import com.kakao.sdk.talk.model.Friend
import okhttp3.MultipartBody
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.groupalbum.data.dto.*
import org.soma.everyonepick.groupalbum.data.entity.*
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumPhotoService
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumPickService
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumResultPhotoService
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumService
import org.soma.everyonepick.groupalbum.domain.model.*
import org.soma.everyonepick.groupalbum.domain.translator.GroupAlbumTranslator.Companion.toGroupAlbumModelList
import org.soma.everyonepick.groupalbum.domain.translator.PhotoTranslator.Companion.toPhotoModelList
import org.soma.everyonepick.groupalbum.domain.translator.PickInfoTranslator.Companion.toPickInfoModel
import org.soma.everyonepick.groupalbum.domain.translator.PickTranslator.Companion.toPickModelList
import org.soma.everyonepick.groupalbum.domain.translator.ResultPhotoTranslator.Companion.toResultPhotoModelList
import org.soma.everyonepick.groupalbum.domain.translator.toUserListWithClientId
import javax.inject.Inject

class GroupAlbumUseCase @Inject constructor(
    private val groupAlbumService: GroupAlbumService,
    private val groupAlbumPhotoService: GroupAlbumPhotoService,
    private val groupAlbumPickService: GroupAlbumPickService,
    private val groupAlbumResultPhotoService: GroupAlbumResultPhotoService
) {
    /** [GroupAlbumService] */
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


    /** [GroupAlbumPhotoService] */
    suspend fun readPhotoList(token: String, id: Long): MutableList<PhotoModel> {
        val photoList = groupAlbumPhotoService.readPhotoList(token, id).data.toMutableList()
        return photoList.toPhotoModelList()
    }

    suspend fun createPhotoList(
        token: String,
        id: Long,
        images: List<MultipartBody.Part>
    ): MutableList<PhotoModel> {
        val photoList = groupAlbumPhotoService.createPhotoList(token, id, images).data.toMutableList()
        return photoList.toPhotoModelList()
    }

    suspend fun deletePhotoList(
        token: String,
        groupAlbumId: Long,
        photoIdList: PhotoIdListRequest
    ) {
        groupAlbumPhotoService.deletePhotoList(token, groupAlbumId, photoIdList)
    }


    /** [GroupAlbumPickService] */
    suspend fun readPickList(token: String, groupAlbumId: Long): MutableList<PickModel> {
        val pickList = groupAlbumPickService.readPickList(token, groupAlbumId).data
        return pickList.toPickModelList().toMutableList()
    }

    suspend fun readPickDetail(token: String, groupAlbumId: Long, pickId: Long): PickDetail {
        return groupAlbumPickService.readPick(token, groupAlbumId, pickId).data
    }

    suspend fun createPick(
        token: String,
        groupAlbumId: Long,
        pickRequest: PickRequest
    ): Pick {
        return groupAlbumPickService.createPick(token, groupAlbumId, pickRequest).data
    }

    suspend fun readPickInfo(token: String, pickId: Long): PickInfoModel {
        val pickInfo = groupAlbumPickService.readPickInfo(token, pickId).data
        return pickInfo.toPickInfoModel()
    }

    suspend fun createPickInfo(
        token: String,
        pickId: Long,
        photoIdList: PhotoIdListRequest
    ): PickInfoModel {
        val pickInfo = groupAlbumPickService.createPickInfo(token, pickId, photoIdList).data
        return pickInfo.toPickInfoModel()
    }

    suspend fun patchPickInfo(
        token: String,
        pickId: Long,
        timeOut: Long
    ) {
        val pickInfoUserRequest = PickInfoUserRequest(timeOut)
        groupAlbumPickService.patchPickInfo(token, pickId, pickInfoUserRequest)
    }


    /** [GroupAlbumResultPhotoService] */
    suspend fun readResultPhotoList(
        token: String,
        groupAlbumId: Long
    ): MutableList<ResultPhotoModel> {
        val data = groupAlbumResultPhotoService.readResultPhotoList(token, groupAlbumId).data
        return data.toResultPhotoModelList()
    }

    suspend fun deleteResultPhotoList(
        token: String,
        groupAlbumId: Long,
        resultPhotoIdList: ResultPhotoIdListRequest
    ) {
        groupAlbumResultPhotoService.deleteResultPhotoList(token, groupAlbumId, resultPhotoIdList)
    }
}