package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.picklist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.groupalbum.R
import org.soma.everyonepick.groupalbum.domain.model.PickModel
import org.soma.everyonepick.groupalbum.domain.usecase.GroupAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class PickListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groupAlbumUseCase: GroupAlbumUseCase,
    private val dataStoreUseCase: DataStoreUseCase
): ViewModel() {
    private val _pickModelList = MutableStateFlow<MutableList<PickModel>>(mutableListOf())
    val pickModelList: StateFlow<MutableList<PickModel>> = _pickModelList

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun readPickModelList(groupAlbumId: Long) {
        viewModelScope.launch {
            try {
                _pickModelList.value = mutableListOf(PickModel(0, false, "http://k.kakaocdn.net/dn/bh32Ln/btrwA0gpWIl/7yGKKShKzEterVMnCKCmD0/img_110x110.jpg"))
            } catch (e: Exception) {
                _toastMessage.value = context.getString(R.string.toast_failed_to_read_pick)
            }
        }
    }
}