package com.example.huckathon.ui
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.huckathon.R
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class HelloWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result = runBlocking {
        try {
            val message = fetchMessageFromApi()
            if (message != null) {
                sendNotification(message)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("HelloWorker", "Error: ${e.message}")
            Result.failure()
        }
    }

    private suspend fun fetchMessageFromApi(): String? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.101.12.18:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NotificationApi::class.java)
        return try {
            val response = api.generateNotification()
            Log.e("EREN", response.toString())
            response.response
        } catch (e: Exception) {
            Log.e("HelloWorker", "API error: ${e.message}")
            null
        }
    }

    private fun sendNotification(message: String) {
        val channelId = "hello_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Hello Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val user = Person.Builder().setName("LümenBot Hatırlatması").build()

        val messagingStyle = NotificationCompat.MessagingStyle(user)
            .addMessage(message, System.currentTimeMillis(), user)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
            .setColor(0xFF6200EE.toInt())
            .setStyle(messagingStyle)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    // Retrofit API interface
    interface NotificationApi {
        @GET("/generateNotification")
        suspend fun generateNotification(): NotificationResponse
    }

    data class NotificationResponse(
        val response: String
    )
}