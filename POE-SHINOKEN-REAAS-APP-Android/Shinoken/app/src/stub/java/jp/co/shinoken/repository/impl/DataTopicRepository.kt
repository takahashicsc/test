package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.Topic
import jp.co.shinoken.repository.TopicRepository
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

class DataTopicRepository @Inject constructor() : TopicRepository {
    val mockTopics = listOf(
        Topic(
            id = 0,
            title = "メディアタイトルメディアタイトルメディアメディアタイトルタイトル",
            date = "2020.12.23",
            image = null,
            url = "https://www.shinoken-fcl.com/nyuukyosha/service/tokutoku/"
        ),
        Topic(
            id = 0,
            title = "メディアタイトルメディアタイトルメディアメディアタイトルタイトル",
            date = "2020.12.23",
            image = null,
            url = "https://www.shinoken-fcl.com/nyuukyosha/service/tokutoku/"
        ),
        Topic(
            id = 0,
            title = "メディアタイトルメディアタイトルメディアメディアタイトルタイトル",
            date = "2020.12.23",
            image = null,
            url = "https://www.shinoken-fcl.com/nyuukyosha/service/tokutoku/"
        )
    )

    override fun getTopics(): ApiResponse<List<Topic>> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = mockTopics,
            appError = null
        )
    }
}