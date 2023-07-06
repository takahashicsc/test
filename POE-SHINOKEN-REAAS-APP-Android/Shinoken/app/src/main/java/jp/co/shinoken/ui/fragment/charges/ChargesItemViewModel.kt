package jp.co.shinoken.ui.fragment.charges

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import jp.co.shinoken.model.Charge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChargesItemViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val charges: StateFlow<List<Charge>> get() = _charges

    private val _charges = MutableStateFlow<List<Charge>>(listOf())

    init {
        val chargeType = savedStateHandle.get<List<Charge>>("chargesType")
        if (chargeType != null) {
            _charges.value = chargeType
        }
    }
}