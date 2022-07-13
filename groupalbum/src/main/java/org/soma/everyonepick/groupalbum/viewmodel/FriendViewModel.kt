package org.soma.everyonepick.groupalbum.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.soma.everyonepick.groupalbum.data.FriendUseCase
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject internal constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {
    val friends: MutableLiveData<Friends<Friend>> = MutableLiveData()
    init {
        updateFriends()
    }

    fun updateFriends() {
        GlobalScope.launch {
            friends.postValue(friendUseCase.getFriends())
        }
    }
}