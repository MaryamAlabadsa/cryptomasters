package com.mas.cryptomasters.core.fcm


import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Navigate

class FcmService : FirebaseMessagingService() {
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var intent: Intent
    private val postActions = listOf("like", "comment")
    override fun onCreate() {
        super.onCreate()
        preferenceHelper = PreferenceHelper(applicationContext)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        var title = getString(R.string.app_name)
        var body = ""


        if (message.data.isNotEmpty()) {
            if (message.data["title"] != null) {
                title = message.data["title"].toString()
            }
            if (message.data["body"] != null) {
                body = message.data["body"].toString()
            }

            val bundle = Bundle()
            intent = Intent(applicationContext, NavigationActivity::class.java)
            if (applicationInForeground() && message.data["post_id"] != null && message.data["action"] != null) {
                when {
                    postActions.indexOf(message.data["action"]) != -1 -> {
                        bundle.putString(Navigate.TARGET, Navigate.POST_DETAILS)
                        bundle.putString(Navigate.DATA_ID, message.data["post_id"])
                        intent.putExtras(bundle)
                    }
                    message.data["action"] == "recommend" -> {
                        bundle.putString(Navigate.TARGET, Navigate.RECOMMEND_FRAGMENT)
                        intent.putExtras(bundle)
                    }
                    else -> {
                        bundle.putString(Navigate.TARGET, Navigate.NOTIFICATION)
                    }
                }
            } else {
                bundle.putString(Navigate.TARGET, Navigate.NOTIFICATION)
            }
            intent.putExtras(bundle)
            sendNotification(title, body)
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferenceHelper.setFCMToken(token)
    }

    private fun sendNotification(title: String, messageBody: String) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setColor(getColor(R.color.them))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "tarteeb.shoping.service",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendNotificationWithoutIntent(title: String, messageBody: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setColor(getColor(R.color.them))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.build().flags =
            notificationBuilder.build().flags or Notification.FLAG_ONGOING_EVENT
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "mediahouse.influencer",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun applicationInForeground(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.runningAppProcesses
        var isActivityFound = false
        if (services[0].processName
                .equals(
                    packageName,
                    ignoreCase = true
                ) && services[0].importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        ) {
            isActivityFound = true
        }
        return isActivityFound
    }


}