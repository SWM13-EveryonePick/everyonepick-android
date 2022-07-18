package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumListItems(): MutableList<GroupAlbumListItem> {
        // TODO: Retrofit2 -> groupAlbumList
        val groupAlbumList = mutableListOf(
            GroupAlbum(0, "title0"),
            GroupAlbum(1, "title1"),
            GroupAlbum(2, "title2"),
            GroupAlbum(3, "title3")
        )
        return convertGroupAlbumListToGroupAlbumListItems(groupAlbumList)
    }

    private fun convertGroupAlbumListToGroupAlbumListItems(groupAlbumList: MutableList<GroupAlbum>): MutableList<GroupAlbumListItem> {
        val groupAlbumListItems = mutableListOf<GroupAlbumListItem>()
        for(i in 0 until groupAlbumList.size) {
            groupAlbumListItems.add(GroupAlbumListItem(groupAlbumList[i], false, false))
        }
        return groupAlbumListItems
    }
}