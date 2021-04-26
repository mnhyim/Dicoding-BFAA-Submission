package com.mnhyim.githubusersapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.mnhyim.githubusersapplication.view.activities.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    private val TAG: String = AlarmReceiver::class.java.simpleName

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive")
        showRepeatingNotification(
            context,
            intent.getStringExtra(EXTRA_TITLE).toString(),
            intent.getStringExtra(EXTRA_MESSAGE).toString()
        )
    }

    private fun showRepeatingNotification(context: Context, title: String, message: String) {
        val channelID = "repeating_notif_id"
        val channelName = "repeating_notif_name"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            context,
            ID_REPEATING,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(notificationPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            mBuilder.setChannelId(channelID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        notificationManagerCompat.notify(ID_REPEATING, mBuilder.build())
    }

    fun setRepeatingAlarm(context: Context, title: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, R.string.reminder_set_toast, Toast.LENGTH_SHORT).show()
    }

    fun unsetRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)

        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, R.string.reminder_unset_toast, Toast.LENGTH_SHORT).show()

    }
}