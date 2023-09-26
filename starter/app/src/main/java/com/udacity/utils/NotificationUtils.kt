package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.activity.MainActivity
import com.udacity.R

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT //decides to use an existing one or create a new one
    )

    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources, R.drawable.tester
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        //.bigLargeIcon(null) //so the large icon goes away when expanded

    // TODO: Step 2.2 add snooze
    //val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    //val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        //applicationContext, REQUEST_CODE, snoozeIntent, FLAGS
    //)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    //this has to match what we assigned it to in the fragment
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_id)
    )

        // TODO: Step 1.8 use the new 'breakfast' notification channel

        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.tester)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true) //notification dismisses itself when you enter the app

        // TODO: Step 2.1 add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)

        // TODO: Step 2.3 add snooze action
        //.addAction(R.drawable.tester, applicationContext.getString(R.string.snooze),
            //snoozePendingIntent)

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH) //have to set this for the older sdk versions

    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())

}

// TODO: Step 1.14 Cancel all notifications
fun NotificationManager.cancelNotifications() {
    cancelAll()
}