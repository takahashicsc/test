package jp.co.shinoken.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.StoreRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val storeRepository: StoreRepository,
) : ViewModel() {
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
        storeRepository.deleteApiTokens()
    }
}