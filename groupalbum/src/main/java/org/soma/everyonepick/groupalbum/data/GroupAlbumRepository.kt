package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumItemList(): MutableList<GroupAlbumItem> {
        // TODO: Retrofit2 -> groupAlbumList
        val groupAlbumDaoList = mutableListOf(
            GroupAlbumDao(0, "title0"),
            GroupAlbumDao(1, "title1"),
            GroupAlbumDao(2, "title2"),
            GroupAlbumDao(3, "title3")
        )
        return convertGroupAlbumDaoListToGroupAlbumItemList(groupAlbumDaoList)
    }

    fun convertGroupAlbumDaoListToGroupAlbumItemList(groupAlbumDaoList: MutableList<GroupAlbumDao>): MutableList<GroupAlbumItem> {
        val groupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumDaoList.size) {
            groupAlbumItemList.add(GroupAlbumItem(groupAlbumDaoList[i], false, false))
        }
        return groupAlbumItemList
    }

    fun getGroupAlbumDao(id: Long): GroupAlbumDao {
        // TODO: Retrofit2... req: id -> res: GroupAlbumDao
        return GroupAlbumDao(
            id, "title$id"
        )
    }
}