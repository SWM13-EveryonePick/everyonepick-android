package org.soma.everyonepick.app.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import org.soma.everyonepick.common.domain.usecase.UserUseCase
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject lateinit var userUseCase: UserUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FcmToken", token)
        // TODO: userUseCase.updateFcmToken(token)
    }
}