package es.upm.bienestaremocional.app.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import es.upm.bienestaremocional.R

enum class AppChannels(val nameId: Int, val channelId: String)
{
    Main(R.string.mainChannelName,"0")
}

fun createNotificationChannel(
    id: String,
    name: String,
    context: Context,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT)
{
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationChannel = NotificationChannel(id, name, importance)

    notificationManager.createNotificationChannel(notificationChannel)
}