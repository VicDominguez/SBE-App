package es.upm.bienestaremocional.app.ui.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import es.upm.bienestaremocional.R


// shows notification with a title and one-line content text
fun showSimpleNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = basicBuilder(
        context = context,
        channelId = channelId,
        textTitle = textTitle,
        textContent = textContent,
        priority = priority
    )

    showNotification(
        context = context,
        notificationId = notificationId,
        builder = builder
    )
}

// shows notification with large text
fun showLargeTextNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = basicBuilder(
        context = context,
        channelId = channelId,
        textTitle = textTitle,
        textContent = textContent,
        priority = priority
    ).setStyle(NotificationCompat.BigTextStyle().bigText(textContent))

    showNotification(
        context = context,
        notificationId = notificationId,
        builder = builder
    )
}

private fun basicBuilder(
    context: Context,
    channelId: String,
    textTitle: String,
    textContent: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) : NotificationCompat.Builder =
    NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.app_logo)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(priority)

private fun showNotification(
    context: Context,
    notificationId: Int,
    builder : NotificationCompat.Builder
) =
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }