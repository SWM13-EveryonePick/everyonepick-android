package org.soma.everyonepick.groupalbum.data.repository

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends

private const val TAG = "FriendRepository"

class FriendRepository {
    fun fetchFriends(
        onAlways: () -> Unit,
        onSuccess: (Friends<Friend>) -> Unit
    ) { fetchFriends(onAlways, {}, onSuccess) }

    fun fetchFriends(
        onAlways: () -> Unit,
        onFailure: (Throwable?) -> Unit,
        onSuccess: (Friends<Friend>) -> Unit
    ) {
        TalkApiClient.instance.friends { newFriends, error ->
            if(error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
                onFailure.invoke(error)
            } else if(newFriends != null) {
                Log.i(TAG, "카카오톡 친구 목록 가져오기 성공")
                onSuccess.invoke(newFriends)

                // No Friends 테스트 코드:
                // onSuccess.invoke(Friends(0, listOf(), 0, "", ""))
            }
            onAlways.invoke()
        }
    }
}