package org.soma.everyonepick.groupalbum.ui.groupalbumlist.groupalbum.timeout

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimeoutViewModel: ViewModel() {
    private val _hour = MutableStateFlow(0)
    private val _min1 = MutableStateFlow(0)
    val min1: StateFlow<Int> = _min1
    private val _min2 = MutableStateFlow(0)

    /**
     * EditText가 전부 채워졌을 때만 확인 버튼을 허용하기 때문에 이를 체크하기 위한 값입니다.
     */
    private val _filledEditText = MutableStateFlow(0)
    val filledEditText: StateFlow<Int> = _filledEditText

    fun setHour(hour: Int) {
        _hour.value = hour
    }

    fun setMin1(min: Int) {
        _min1.value = min
    }

    fun setMin2(min: Int) {
        _min2.value = min
    }

    fun plusFilledEditText() {
        _filledEditText.value += 1
    }

    fun minusFilledEditText() {
        _filledEditText.value -= 1
    }

    fun calculateTimeoutAsMin() = _hour.value*60 + _min1.value*10 + _min2.value
}