package org.soma.everyonepick.groupalbum.ui.friendlist
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.soma.everyonepick.common.util.KakaoUtil
import org.soma.everyonepick.common.domain.usecase.FriendUseCase
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {
    private val _friends: MutableStateFlow<Friends<Friend>> = MutableStateFlow(KakaoUtil.emptyFriends)
    val friends: StateFlow<Friends<Friend>> = _friends

    private val _isApiLoading = MutableStateFlow(true)
    val isApiLoading: StateFlow<Boolean> = _isApiLoading

    fun readFriends() {
        _isApiLoading.value = true
        friendUseCase.readFriends({ _isApiLoading.value = false }) { newFriends ->
            _friends.value = newFriends
        }
    }
}