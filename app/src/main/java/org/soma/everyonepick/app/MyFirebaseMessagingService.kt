package org.soma.everyonepick.app

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.soma.everyonepick.common.util.NotificationUtil

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            // TODO: deep link using payload
        }

        message.notification?.let {
            NotificationUtil.sendNotification(this, it.title ?: "", it.body ?: "")
        }
    }
}
