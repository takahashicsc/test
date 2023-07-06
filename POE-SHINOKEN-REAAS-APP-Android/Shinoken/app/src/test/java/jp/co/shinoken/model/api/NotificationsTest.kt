package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class NotificationsTest {
    private lateinit var notifications: Notifications

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("notifications.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        notifications = moshi.adapter(Notifications::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getNotifications() {
        assertEquals(42, notifications.total)
        assertEquals(41, notifications.unreadCount)
        val notification = notifications.data.first()

        assertEquals(
            Notification(
                id = 33,
                notificationPublishId = 6,
                title = "リニューアルのお知らせ ntfcd131d50",
                contentText = "<p>平素は格別のご愛顧を賜り、厚くお礼申し上げます。 誠に勝手ながら○月○日（○）～○月○日（○）まで連休とさせていただきます。</p><p>なお、設備の故障など緊急のお問い合わせに関しましては、何卒ご理解いただきますよう順次回答させて頂きます。</p>",
                read = true,
                images = listOf(),
                labels = listOf(),
                links = listOf(
                    Link(
                        kind = Kind.DeepLink,
                        title = "マイページ",
                        url = "shinoken-residentapp-demo:///mypage"
                    )
                ),
                segmentedAt = "2021-02-08T10:31:49.000Z"
            ), notification
        )

        assertEquals(
            "2021.02.08", notification.getSegmentedAtFormatString()
        )

    }
}
