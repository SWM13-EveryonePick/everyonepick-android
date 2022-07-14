package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumList(): MutableList<GroupAlbum> {
        // TODO: Retrofit2
        val groupAlbumList = mutableListOf(
            GroupAlbum(0, "title0"),
            GroupAlbum(1, "title1"),
            GroupAlbum(2, "title2"),
            GroupAlbum(3, "title3")
        )
        return groupAlbumList
    }
}