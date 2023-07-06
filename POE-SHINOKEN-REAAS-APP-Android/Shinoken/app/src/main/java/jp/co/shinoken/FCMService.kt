package jp.co.shinoken

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import jp.co.shinoken.activity.LauncherActivity
import jp.co.shinoken.repository.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject
    lateinit var storeRepository: StoreRepository

    override fun onMessageReceived(message: RemoteMessage) {
        sendNotification(message.notification, message.data)
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: MutableMap<String, String>
    ) {
        val intent = LauncherActivity.createIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val title: String? = notification?.title
        val imageUri: Uri? = notification?.imageUrl
        val messageBody: String? = notification?.body

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, getString(R.string.push_channel_info_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(ContextCompat.getColor(this, R.color.gray))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent)
        imageUri?.let {
            notificationBuilder.applyImageUrl(imageUrl = it.toString())
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.notify(0, notificationBuilder.build())
    }

    private fun NotificationCompat.Builder.applyImageUrl(
        imageUrl: String
    ) = runBlocking {
        val url = URL(imageUrl)

        withContext(Dispatchers.IO) {
            try {
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }?.let { bitmap ->
            setLargeIcon(bitmap)
            setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        storeRepository.savePushToken(pushToken = token, isPost = false)
    }
}