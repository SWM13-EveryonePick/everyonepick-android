package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.groupalbumtitle

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.util.KeyboardUtil
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class GroupAlbumTitleViewModel @Inject constructor(
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    val title = MutableStateFlow("")

    fun createGroupAlbum(userList: List<User>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = dataStoreUseCase.bearerAccessToken.first()!!
                val groupAlbum = GroupAlbum(title = title.value, users = userList)
                groupAlbumUseCase.createGroupAlbum(token, groupAlbum)

                onSuccess.invoke()
            } catch (e: Exception) {
                _toastMessage.value = "단체공유앨범 생성에 실패하였습니다."
            }
        }
    }
}