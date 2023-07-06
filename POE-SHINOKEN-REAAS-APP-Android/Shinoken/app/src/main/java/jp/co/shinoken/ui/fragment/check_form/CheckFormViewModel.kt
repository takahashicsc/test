package jp.co.shinoken.ui.fragment.check_form

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import jp.co.shinoken.api.Status
import jp.co.shinoken.api.request.RequestCheckForm
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.CheckCategory
import jp.co.shinoken.model.CheckItem
import jp.co.shinoken.model.api.Image
import jp.co.shinoken.repository.CheckFormRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CheckFormViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val checkFormRepository: CheckFormRepository
) :
    ViewModel() {
    val formCheckItem: StateFlow<List<CheckCategory>>
        get() = _formCheckItem

    private val _formCheckItem: MutableStateFlow<List<CheckCategory>> =
        MutableStateFlow(savedStateHandle.get(CheckFormFragment.ArgCheckFormData) ?: listOf())

    val deadline: MutableStateFlow<String?> get() = _deadline
    private val _deadline = MutableStateFlow<String?>(null)

    var isSubmit: Boolean = false
        private set

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val putAppError: StateFlow<AppError?>
        get() = _putAppError
    private val _putAppError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    val isCheckedForm =
        savedStateHandle.get<Boolean>(CheckFormFragment.ArgIsCheckedForm) ?: false


    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    var checkItemSlug: String = ""

    fun fetch() {
        _apiState.value = ApiState.Empty
        viewModelScope.launch {
            _apiState.value = ApiState.LOADING
            val response = checkFormRepository.getCheckForms()
            _apiState.value = ApiState.Empty
            if (response.status == Status.SUCCESS) {
                isSubmit = response.data!!.submittable.not()
                _deadline.value = response.data.getDeadlineFormatString()
                _formCheckItem.value = CheckCategory.convertApiResponse(response.data)
                validation()
                savedStateHandle.set(CheckFormFragment.ArgCheckFormData, formCheckItem.value)
                savedStateHandle.set(CheckFormFragment.ArgIsCheckedForm, true)
            } else {
                _appError.value = response.appError ?: return@launch
            }
        }
    }

    fun postData(isCheck: Boolean) {
        _apiState.value = ApiState.LOADING
        viewModelScope.launch {
            val response = checkFormRepository.postCheckFormSubmit(isCheck)

            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
                isSubmit = true
            } else {
                _putAppError.value = response.appError
                _apiState.value = ApiState.Empty
            }
        }
    }

    suspend fun setFormImages(postFile: File?) {
        postFile ?: return
        val replaceCheckFormItem =
            formCheckItem.value.firstOrNull { it.checkItems.firstOrNull { checkItem -> checkItem.slug == checkItemSlug } != null }
                ?: return

        val image = postImage(file = postFile)

        image ?: return

        val replaceImages =
            replaceCheckFormItem.checkItems.first { it.slug == checkItemSlug }.images.toMutableList()
        replaceImages.add(image)

        replaceCheckFormItem.checkItems.first { it.slug == checkItemSlug }.images =
            replaceImages

        viewModelScope.launch {
            val isPutSuccess =
                putCheckForm(
                    replaceCheckFormItem.checkItems.first { it.slug == checkItemSlug }
                )
            if (isPutSuccess.not()) return@launch

            _formCheckItem.value = formCheckItem.value.map { checkCategory ->
                if (checkCategory.checkItems.firstOrNull { it.slug == checkItemSlug } != null) {
                    replaceCheckFormItem
                } else {
                    checkCategory
                }
            }
        }
    }

    fun removeImage(image: Image, checkItem: CheckItem) {
        val replaceCheckFormItem =
            formCheckItem.value.firstOrNull { checkCategory -> checkCategory.checkItems.firstOrNull { it == checkItem } != null }
                ?: return

        replaceCheckFormItem.checkItems

        viewModelScope.launch {
            val replaceImages = checkItem.images.toMutableList()
            replaceImages.remove(image)
            replaceCheckFormItem.checkItems.first { it == checkItem }.images = replaceImages

            val isPutSuccess =
                putCheckForm(replaceCheckFormItem.checkItems.first { it == checkItem })
            if (isPutSuccess.not()) return@launch

            _formCheckItem.value = formCheckItem.value.map { checkCategory ->
                if (checkCategory.checkItems.firstOrNull { it.slug == checkItemSlug } != null) {
                    replaceCheckFormItem
                } else {
                    checkCategory
                }
            }
        }
    }

    fun updateText(text: String, checkItem: CheckItem) {
        val replaceCheckFormItem =
            formCheckItem.value.firstOrNull { checkCategory -> checkCategory.checkItems.firstOrNull { it == checkItem } != null }

        replaceCheckFormItem?.checkItems ?: return

        viewModelScope.launch {
            val isPutSuccess =
                putCheckForm(replaceCheckFormItem.checkItems.first { it == checkItem })

            if (isPutSuccess.not()) return@launch
            replaceCheckFormItem.checkItems.first { it == checkItem }.description = text

            _formCheckItem.value = formCheckItem.value.map { checkCategory ->
                if (checkCategory.checkItems.firstOrNull { it == checkItem } != null) {
                    replaceCheckFormItem
                } else {
                    checkCategory
                }
            }
        }
    }

    fun updateCheck(checkItem: CheckItem) {
        val replaceCheckCategory =
            formCheckItem.value.first { checkCategory -> checkCategory.checkItems.firstOrNull { it == checkItem } != null }

        viewModelScope.launch {
            replaceCheckCategory.checkItems.first { it == checkItem }.result = checkItem.result

            val isPutSuccess =
                putCheckForm(replaceCheckCategory.checkItems.first { it == checkItem })
            if (isPutSuccess.not()) return@launch

            _formCheckItem.value = formCheckItem.value.map { checkCategory ->
                if (checkCategory.checkItems.firstOrNull { it == checkItem } != null) {
                    replaceCheckCategory
                } else {
                    checkCategory
                }
            }

            validation()
        }
    }

    private suspend fun postImage(file: File): Image? {
        val response = checkFormRepository.postImage(
            file = file
        )

        return if (response.status == Status.SUCCESS) {
            response.data?.data
        } else {
            _putAppError.value = response.appError
            null
        }
    }

    private suspend fun putCheckForm(checkItem: CheckItem): Boolean {
        val requestCheckForm = RequestCheckForm(
            result = checkItem.result,
            description = checkItem.description,
            image1 = if (checkItem.images.isNotEmpty()) checkItem.images[0].key else null,
            image2 = if (checkItem.images.size > 1) checkItem.images[1].key else null,
            image3 = if (checkItem.images.size > 2) checkItem.images[2].key else null
        )
        val response = checkFormRepository.putCheckForm(
            slug = checkItem.slug,
            requestCheckForms = requestCheckForm
        )

        if (response.status != Status.SUCCESS) {
            _putAppError.value = response.appError
            return false
        }
        return true
    }

    fun validation() {
        _isButtonEnable.value = isSubmit.not() && formCheckItem.value.flatMap { it.checkItems }
            .firstOrNull { it.result == null } == null
    }
}