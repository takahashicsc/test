package jp.co.shinoken.ui.fragment.contract_update

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.repository.MetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContractUpdateViewModel @ViewModelInject constructor(private val metaRepository: MetaRepository) :
    ViewModel() {
    val privacyPolicyUrl: StateFlow<String?> get() = _privacyPolicyUrl
    private val _privacyPolicyUrl = MutableStateFlow<String?>(null)

    init {
        getMetaData()
    }

    private fun getMetaData() {
        viewModelScope.launch {
            val response = metaRepository.getMeta()

            if (response.status == Status.SUCCESS) {
                _privacyPolicyUrl.value = response.data!!.data.privacyPolicy
            }
        }
    }
}