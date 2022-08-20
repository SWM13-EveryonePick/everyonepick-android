package org.soma.everyonepick.setting.ui

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.soma.everyonepick.common.data.entity.User
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    dataStoreUseCase: DataStoreUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private var bearerAccessToken = dataStoreUseCase.bearerAccessToken

    var me: StateFlow<User> = bearerAccessToken.transformLatest {
        if (it != null) emit(userUseCase.readUser(it))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), User.dummyData)
}