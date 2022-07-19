package org.soma.everyonepick.groupalbum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.soma.everyonepick.groupalbum.data.GroupAlbumDao

class GroupAlbumViewPagerViewModel: ViewModel() {
    val groupAlbum = MutableLiveData(GroupAlbumDao(-1, "Loading"))
}