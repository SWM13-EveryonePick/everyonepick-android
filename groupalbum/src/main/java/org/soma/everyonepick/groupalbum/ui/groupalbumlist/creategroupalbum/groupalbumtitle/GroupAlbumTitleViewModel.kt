package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.groupalbumtitle

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GroupAlbumTitleViewModel: ViewModel() {
    val title = MutableStateFlow("")
}