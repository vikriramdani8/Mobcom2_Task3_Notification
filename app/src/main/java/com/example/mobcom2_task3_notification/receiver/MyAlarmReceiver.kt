package com.example.mobcom2_task3_notification.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobcom2_task3_notification.MainActivity
import com.example.mobcom2_task3_notification.R
import io.karn.notify.Notify
import java.util.*

class MyAlarmReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle : Bundle? = intent!!.extras
        var message = bundle!!.getString("message")
        buildNotification(context!!, "Reminder Alert!", message.toString())
    }

    private fun buildNotification(context: Context, title: String, message: String){
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0, intent, 0)
//        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.android1)
        val bitmapLargeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.evos)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.evos)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(bitmapLargeIcon)
//                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(Random().nextInt(), builder.build())
        }

    }

}