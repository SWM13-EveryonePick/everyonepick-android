package org.soma.everyonepick.groupalbum.ui.friendlist
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {
    val friends: MutableLiveData<Friends<Friend>> = MutableLiveData()
    val isApiLoading = MutableLiveData(true)

    fun fetchFriends() {
        isApiLoading.value = true
        friendUseCase.fetchFriends({ isApiLoading.value = false }) { newFriends ->
            friends.postValue(newFriends)
        }
    }
}