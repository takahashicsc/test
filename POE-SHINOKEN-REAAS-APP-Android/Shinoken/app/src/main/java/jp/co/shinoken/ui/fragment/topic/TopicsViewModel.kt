package jp.co.shinoken.ui.fragment.topic

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.Topic
import jp.co.shinoken.repository.TopicRepository

class TopicsViewModel @ViewModelInject constructor(private val topicRepository: TopicRepository) :
    ViewModel() {

    val topics: LiveData<List<Topic>>
        get() = _topics
    private val _topics = MutableLiveData<List<Topic>>()

    fun fetch() {
        val response = topicRepository.getTopics()
        if (response.status == Status.SUCCESS) {
            _topics.value = response.data ?: return
        } else {
            //TODO: エラー処理
        }
    }
}