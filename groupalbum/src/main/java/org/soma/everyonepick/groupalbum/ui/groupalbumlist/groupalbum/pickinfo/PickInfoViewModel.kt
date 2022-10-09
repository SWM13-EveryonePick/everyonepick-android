package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.pickinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PickInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val userCount = savedStateHandle[USER_COUNT]?: 0
    val pickUserCount = savedStateHandle[PICK_USER_COUNT]?: 0

    private val _timeout = MutableStateFlow(savedStateHandle[TIMEOUT]?: 0L)
    val timeout: StateFlow<Long> = _timeout

    init {
        viewModelScope.launch {
            // Count down to 0
            for (i in _timeout.value downTo 0) {
                _timeout.value = i
                delay(1000)
            }
        }
    }

    companion object {
        private const val USER_COUNT = "userCount"
        private const val PICK_USER_COUNT = "pickUserCount"
        private const val TIMEOUT = "timeout"
    }
}