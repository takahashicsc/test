package jp.co.shinoken.ui.fragment.walk_through

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import jp.co.shinoken.repository.StoreRepository

class WalkThroughViewModel @ViewModelInject constructor(private val storeRepository: StoreRepository) :
    ViewModel() {

    val isWalkThrough = storeRepository.isWalkThrough()

    fun saveWalkThrough() {
        storeRepository.saveWalkThrough()
    }
}