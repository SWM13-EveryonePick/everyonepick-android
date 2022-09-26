package org.soma.everyonepick.groupalbum.ui.groupalbumlist.creategroupalbum.groupalbumtitle

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbum
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class GroupAlbumTitleViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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
                _toastMessage.value = context.getString(R.string.toast_failed_to_create_group_album)
            }
        }
    }
}