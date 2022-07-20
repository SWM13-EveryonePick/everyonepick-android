package org.soma.everyonepick.groupalbum.data

class PhotoRepository {
    fun getPhotoItemList(groupAlbumId: Long): MutableList<PhotoItem> {
        // TODO: Retrofit2 -> photoDaoList
        val photoDaoList = mutableListOf(
            PhotoDao(0, "https://picsum.photos/200"),
            PhotoDao(1, "https://picsum.photos/200"),
            PhotoDao(2, "https://picsum.photos/200"),
            PhotoDao(3, "https://picsum.photos/200")
        )
        return convertPhotoDaoListToPhotoItemList(photoDaoList)
    }

    fun convertPhotoDaoListToPhotoItemList(photoDaoList: MutableList<PhotoDao>): MutableList<PhotoItem> {
        val photoItemList = mutableListOf<PhotoItem>()
        for(i in 0 until photoDaoList.size) {
            photoItemList.add(PhotoItem(photoDaoList[i], false, false))
        }
        return photoItemList
    }
}