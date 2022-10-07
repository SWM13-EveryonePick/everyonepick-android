package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pickinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.soma.everyonepick.groupalbum.domain.model.PickInfoModel
import javax.inject.Inject

@HiltViewModel
class PickInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val userCount = savedStateHandle[USER_COUNT]?: 0
    val pickUserCount = savedStateHandle[PICK_USER_COUNT]?: 0

    private val _timeout = MutableStateFlow(savedStateHandle[TIMEOUT]?: 0L)
    val timeout: StateFlow<Long> = _timeout

    init {
        // TODO: timeout 감소
    }

    companion object {
        private const val USER_COUNT = "userCount"
        private const val PICK_USER_COUNT = "pickUserCount"
        private const val TIMEOUT = "timeout"
    }
}