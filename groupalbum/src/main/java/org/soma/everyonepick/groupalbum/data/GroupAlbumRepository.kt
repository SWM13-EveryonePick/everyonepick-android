package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumItemList(): MutableList<GroupAlbumItem> {
        // TODO: Retrofit2 -> groupAlbumList
        val groupAlbumList = mutableListOf(
            GroupAlbum(0, "title0"),
            GroupAlbum(1, "title1"),
            GroupAlbum(2, "title2"),
            GroupAlbum(3, "title3")
        )
        return convertGroupAlbumListToGroupAlbumItemList(groupAlbumList)
    }

    private fun convertGroupAlbumListToGroupAlbumItemList(groupAlbumList: MutableList<GroupAlbum>): MutableList<GroupAlbumItem> {
        val groupAlbumItemList = mutableListOf<GroupAlbumItem>()
        for(i in 0 until groupAlbumList.size) {
            groupAlbumItemList.add(GroupAlbumItem(groupAlbumList[i], false, false))
        }
        return groupAlbumItemList
    }
}