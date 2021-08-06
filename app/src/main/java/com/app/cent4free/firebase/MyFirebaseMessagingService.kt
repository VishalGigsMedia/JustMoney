package com.app.cent4free.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.offer_details.OfferDetailsFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.quickstart.fcm.kotlin.MyWorker
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.jar.Manifest

class MyFirebaseMessagingService : FirebaseMessagingService() {


    private var notificationType: String = ""
    private var image: String = ""
    private var bitmap: Bitmap? = null
    private var title: String = ""
    private var description: String = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("notification", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("notification", "Message data payload: ${remoteMessage.data}")

            if (remoteMessage.data.containsKey("title")) {
                if (remoteMessage.data.getValue("title").isNotEmpty()) {
                    title = decrypt(remoteMessage.data.getValue("title"))
                }
            }

            if (remoteMessage.data.containsKey("body")) {
                if (remoteMessage.data.getValue("body").isNotEmpty()) {
                    description = decrypt(remoteMessage.data.getValue("body"))
                }
            }

            if (remoteMessage.data.containsKey("notification_type")) {
                if (remoteMessage.data.getValue("notification_type").isNotEmpty()) {
                    notificationType = decrypt(remoteMessage.data.getValue("notification_type"))
                }
            } else {
                notificationType = "0"
            }

            if (remoteMessage.data.containsKey("image")) {
                if (remoteMessage.data.getValue("image").isNotEmpty()) {
                    image = remoteMessage.data.getValue("image")
                    //val notificationType = "0"
                    bitmap = getBitmapFromUrl(decrypt(image))
                }
            }

            if (bitmap != null) {
                showImageNotification(
                    bitmap,
                    title,
                    description,
                    notificationType
                )
            } else {
                sendNotification(
                    title,
                    description,
                    notificationType
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("notification", "Refreshed token: $token")
    }
    private fun showImageNotification(
        bitmap: Bitmap?,
        title: String,
        message: String,
        type: String
    ) {
        var intent: Intent? = null
        // println("notificationType : $notificationType")
        /*0 : Listing
        1 : Offer Detail
        2 : Earnings
        3 : My profile
        4 : PlayStore
        5 : Invite*/
        when (type) {
            "0" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "1" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "2" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "3" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "4" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "5" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            else ->{
                intent = Intent(this, MainActivity::class.java)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) //PendingIntent.FLAG_ONE_SHOT

        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        /*  val pendingIntent = PendingIntent.getActivity(
              this, 0, intent,
              PendingIntent.FLAG_ONE_SHOT
          )*/
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(bitmap)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(this, channelId).setTicker(title).setWhen(0)
            .setAutoCancel(true).setContentTitle(title).setContentText(message)
            .setContentIntent(pendingIntent).setSound(defaultSoundUri).setStyle(bigPictureStyle)
            .setColor(resources.getColor(R.color.color_primary))
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .setContentText(message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = message
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
        val notificationID = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
        notificationManager.notify(notificationID, mBuilder.build())
    }

    private fun sendNotification(
        title: String,
        description: String,
        notificationType: String
    ) {

        var intent: Intent? = null
        //println("notificationType : $notificationType")
        /*0 : Listing
        1 : Offer Detail
        2 : Earnings
        3 : My profile
        4 : PlayStore
        5 : Invite*/
        when (notificationType) {
            "0" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "1" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "2" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "3" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "4" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            "5" -> {
                intent = Intent(this, MainActivity::class.java)
            }
            else -> {
                intent = Intent(this, MainActivity::class.java)
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) //PendingIntent.FLAG_ONE_SHOT

        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //val notificationBuilder = NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(DefaultHelper.decrypt(title)).setContentText(DefaultHelper.decrypt(description)).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.logo)
                .setContentTitle(title).setContentText(description).setAutoCancel(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val url = URL("" + imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)

        } catch (e: Exception) {
            null
        }
    }
}
