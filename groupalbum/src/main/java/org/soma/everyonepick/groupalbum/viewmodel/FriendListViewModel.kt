package org.soma.everyonepick.groupalbum.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendListViewModel: ViewModel() {
    val friends: MutableLiveData<Friends<Friend>> = MutableLiveData()
    val isApiLoading = MutableLiveData(true)
    init {
        fetchFriends()
    }

    fun fetchFriends() {
        isApiLoading.value = true

        TalkApiClient.instance.friends { newFriends, error ->
            if(error != null) {
                Log.e("TAG", "카카오톡 친구 목록 가져오기 실패", error)
            } else if(newFriends != null) {
                Log.i("TAG", "카카오톡 친구 목록 가져오기 성공")
                friends.postValue(newFriends)
            }

            isApiLoading.value = false
        }
    }
}