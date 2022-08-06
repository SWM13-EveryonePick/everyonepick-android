package org.soma.everyonepick.groupalbum.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import org.soma.everyonepick.groupalbum.data.repository.FriendRepository
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {
    val friends: MutableLiveData<Friends<Friend>> = MutableLiveData()
    val isApiLoading = MutableLiveData(true)

    fun fetchFriends() {
        isApiLoading.value = true
        friendRepository.fetchFriends({ isApiLoading.value = false }) { newFriends ->
            friends.postValue(newFriends)
        }
    }
}