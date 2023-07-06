package jp.co.shinoken.repository

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.model.Topic

interface TopicRepository {
    fun getTopics(): ApiResponse<List<Topic>>
}