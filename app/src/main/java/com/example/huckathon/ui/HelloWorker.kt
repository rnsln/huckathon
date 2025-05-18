package com.example.huckathon.ui
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.huckathon.R

class HelloWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "hello_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Hello Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val user = Person.Builder()
            .setName("LümenBot Hatırlatması")
            .build()

        val longText = "Merhaba! Bu, LümenBot'tan gelen uzun bir hatırlatma mesajıdır. " +
                "Bugün kendinize zaman ayırmayı unutmayın. Sağlığınız ve mutluluğunuz bizim için önemli. " +
                "Daha fazla bilgi için uygulamayı ziyaret edin!"

        val messagingStyle = NotificationCompat.MessagingStyle(user)
            .addMessage(longText, System.currentTimeMillis(), user)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setLargeIcon(android.graphics.BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
            .setColor(0xFF6200EE.toInt()) // Example color
            .setStyle(messagingStyle)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}