package org.soma.everyonepick.common.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.soma.everyonepick.common.R

object NotificationUtil {
    private const val CHANNEL_ID = "channel_id"
    private const val NOTIFICATION_ID = 0

    /**
     * @param activityToStart 알림 클릭 시 시작할 액티비티입니다
     */
    fun sendNotification(
        context: Context,
        activityToStart: Class<*>,
        title: String,
        text: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, activityToStart).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon_silhouette)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder)
        }
    }

    /**
     * Android 8.0 이상에서 알림을 제공하려면 Channel을 등록해야 합니다.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}