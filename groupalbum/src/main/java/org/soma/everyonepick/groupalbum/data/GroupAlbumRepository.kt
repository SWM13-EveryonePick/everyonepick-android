package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumItemList(): MutableList<GroupAlbumItem> {
        // TODO: Retrofit2 -> groupAlbumList
        val groupAlbumDaoList = mutableListOf(
            GroupAlbumDao(0, "title0", 100),
            GroupAlbumDao(1, "title1", 101),
            GroupAlbumDao(2, "title2", 102),
            GroupAlbumDao(3, "title3", 103)
        )
        return convertGroupAlbumDaoListToGroupAlbumItemList(groupAlbumDaoList)
    }

    fun convertGroupAlbumDaoListToGroupAlbumItemList(groupAlbumDaoList: MutableList<GroupAlbumDao>): MutableList<GroupAlbumItem> {
        val groupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumDaoList.size) {
            groupAlbumItemList.add(GroupAlbumItem(groupAlbumDaoList[i], false, false))
        }
        // 마지막 아이템으로 더미데이터 -> CreateGroupAlbumItem
        groupAlbumItemList.add(GroupAlbumItem(GroupAlbumDao(-1, "", 0), false, false))
        return groupAlbumItemList
    }

    fun getGroupAlbumDao(id: Long): GroupAlbumDao {
        // TODO: Retrofit2... req: id -> res: GroupAlbumDao
        return GroupAlbumDao(
            id, "title$id", 100+id.toInt()
        )
    }
}