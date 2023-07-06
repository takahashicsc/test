package jp.co.shinoken.ui.fragment.setting

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.repository.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel @ViewModelInject constructor(
    private val metaRepository: MetaRepository
) : ViewModel() {
    val termsUrl: StateFlow<String?> get() = _termsUrl
    private val _termsUrl = MutableStateFlow<String?>(null)

    init {
        getMetaData()
    }

    private fun getMetaData() {
        viewModelScope.launch {
            val response = metaRepository.getMeta()

            if (response.status == Status.SUCCESS) {
                _termsUrl.value = response.data!!.data.termOfService
            }
        }
    }
}