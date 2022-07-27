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
    // 초기에는 리스트는 비었지만 작업을 처리중인 것이기에, "No Friends"를 띄우는 게 아니라
    // ProgressBar를 띄우는 것이 적합합니다.
    // loading |-- Friends
    //          `- "No Friends"
    val isApiLoading = MutableLiveData(true)
    init {
        fetchFriends()
    }

    fun fetchFriends() {
        isApiLoading.value = true
        friendRepository.fetchFriends({ isApiLoading.value = false }) { newFriends ->
            friends.postValue(newFriends)
        }
    }
}