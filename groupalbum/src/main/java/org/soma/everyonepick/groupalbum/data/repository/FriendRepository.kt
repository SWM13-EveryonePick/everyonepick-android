package org.soma.everyonepick.groupalbum.data.repository

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import org.soma.everyonepick.common.util.KakaoUtil

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
            if (error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
                onFailure.invoke(error)
            } else if (newFriends != null) {
                Log.i(TAG, "카카오톡 친구 목록 가져오기 성공")
                // onSuccess.invoke(newFriends)

                // TEST - Empty Friends:
                // onSuccess.invoke(KakaoUtil.emptyFriends)

                // TEST - Many Friends:
                onSuccess.invoke(KakaoUtil.manyFriends)
            }
            onAlways.invoke()
        }
    }

    companion object {
        private const val TAG = "FriendRepository"
    }
}