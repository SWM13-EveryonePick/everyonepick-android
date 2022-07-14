package org.soma.everyonepick.groupalbum.data

class GroupAlbumRepository {
    fun getGroupAlbumList(): List<GroupAlbum> {
        // TODO: Retrofit2
        val groupAlbumList = listOf(
            GroupAlbum(0, "title0"),
            GroupAlbum(1, "title1"),
            GroupAlbum(2, "title2"),
            GroupAlbum(3, "title3")
        )
        return groupAlbumList
    }
}