package org.soma.everyonepick.app

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.soma.everyonepick.app.ui.HomeActivity
import org.soma.everyonepick.common.util.NotificationUtil
import org.soma.everyonepick.login.ui.SplashActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            // TODO: deep link using payload
            Log.d("FCM", message.data.toString())
        }

        message.notification?.let {
            NotificationUtil.sendNotification(this, SplashActivity::class.java, it.title ?: "", it.body ?: "")
        }
    }
}
