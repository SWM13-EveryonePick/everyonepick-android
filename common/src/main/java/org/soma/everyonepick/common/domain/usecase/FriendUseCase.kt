package org.soma.everyonepick.common.domain.usecase

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends

/**
 * 카카오톡 API를 이용하여 [Friends]를 제공합니다. 카카오톡 API에서 Flow 같은 값을 반환하는 대신 콜백 함수를 제공하기 때문에
 * 각 호출 함수의 인자에 콜백이 들어가는 것이 불가피합니다.
 */
class FriendUseCase {
    fun readFriends(onAlways: () -> Unit) {
        readFriends(onAlways, {}, {})
    }

    fun readFriends(
        onAlways: () -> Unit,
        onSuccess: (Friends<Friend>) -> Unit
    ) {
        readFriends(onAlways, {}, onSuccess)
    }

    fun readFriends(
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
                onSuccess.invoke(newFriends)

                // TEST - Empty Friends:
                // onSuccess.invoke(KakaoUtil.emptyFriends)

                // TEST - Many Friends:
                // onSuccess.invoke(KakaoUtil.manyFriends)
            }
            onAlways.invoke()
        }
    }

    companion object {
        private const val TAG = "FriendUseCase"
    }
}