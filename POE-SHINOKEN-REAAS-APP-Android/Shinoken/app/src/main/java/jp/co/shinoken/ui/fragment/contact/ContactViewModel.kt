package jp.co.shinoken.ui.fragment.contact

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.api.Status
import jp.co.shinoken.api.request.RequestInquiryForm
import jp.co.shinoken.model.ApiState
import jp.co.shinoken.model.AppError
import jp.co.shinoken.model.OfficeName.Companion.getOfficeName
import jp.co.shinoken.model.api.Home
import jp.co.shinoken.model.api.Inquiries
import jp.co.shinoken.model.api.Suggestion
import jp.co.shinoken.repository.ContactRepository
import jp.co.shinoken.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel @ViewModelInject constructor(
    private val contactRepository: ContactRepository,
    private val homeRepository: HomeRepository
) :
    ViewModel() {

    val categories: StateFlow<List<Inquiries.Kind>>
        get() = _categories

    private val _categories = MutableStateFlow<List<Inquiries.Kind>>(listOf())

    val ages: StateFlow<List<String>>
        get() = _ages

    private val _ages = MutableStateFlow<List<String>>(listOf())

    val privacyPolicyUrl: StateFlow<String?> get() = _privacyPolicyUrl
    private val _privacyPolicyUrl = MutableStateFlow<String?>(null)

    val suggestions: StateFlow<List<Suggestion>> get() = _suggestions
    private val _suggestions = MutableStateFlow<List<Suggestion>>(listOf())

    val name: StateFlow<String?> get() = _name
    private val _name = MutableStateFlow<String?>(null)

    val buildingName: StateFlow<String?> get() = _buildingName
    private val _buildingName = MutableStateFlow<String?>(null)

    val buildingAddress: StateFlow<String?> get() = _buildingAddress
    private val _buildingAddress = MutableStateFlow<String?>(null)

    val roomNumber: StateFlow<String?> get() = _roomNumber
    private val _roomNumber = MutableStateFlow<String?>(null)

    val emailAddress: StateFlow<String?> get() = _emailAddress
    private val _emailAddress = MutableStateFlow<String?>(null)

    val telnumber: StateFlow<String?> get() = _telnumber
    private val _telnumber = MutableStateFlow<String?>(null)

    val prefectures: StateFlow<List<String>>
        get() = _prefectures

    private val _prefectures = MutableStateFlow<List<String>>(listOf())

    val profession: StateFlow<List<String>>
        get() = _profession

    private val _profession = MutableStateFlow<List<String>>(listOf())

    val appError: StateFlow<AppError?>
        get() = _appError
    private val _appError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val apiState: StateFlow<ApiState> get() = _apiState
    private val _apiState = MutableStateFlow(ApiState.Empty)

    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _isLoading = MutableStateFlow(false)

    var selectCategory: String = ""
    var contactName: String = ""
    var contactBuildingName: String = ""
    var contactBuildingAddress: String = ""
    var contactRoomNumber: String = ""
    var selectPrefectures:String = ""
    var selectAge:String = ""
    var selectSex:String = ""
    var selectProfession:String = ""
    var email: String = ""
    var tel: String = ""
    var body: String = ""

    val isButtonEnable: StateFlow<Boolean> get() = _isButtonEnable
    private val _isButtonEnable = MutableStateFlow(false)

    init {
        fetch()
        getHomeData()
    }

    private fun getHomeData() {
        _appError.value = null
        viewModelScope.launch {
            val response = homeRepository.getHome()

            if (response.status == Status.SUCCESS) {
                _privacyPolicyUrl.value = response.data!!.me.meta.privacyPolicy
                _emailAddress.value = response.data.me.email
                _telnumber.value = if (response.data.me.tel.toString() == "null")  "" else response.data.me.tel.toString()
                _suggestions.value = response.data.suggestions
                _name.value = response.data.me.name
                _buildingName.value = response.data.me.building.name
                _buildingAddress.value = response.data.me.building.address
                _roomNumber.value = response.data.me.room.number
            } else {
                _appError.value = response.appError
            }
        }
    }

    fun fetch() {
        _isLoading.value = true
        _appError.value = null
        viewModelScope.launch {
            val response = contactRepository.getInquiries()
            if (response.status == Status.SUCCESS) {
                _categories.value = response.data!!.kinds
            } else {
                _appError.value = response.appError
            }
            _isLoading.value = false
        }
    }


    fun validation() {
        _isButtonEnable.value =
            selectCategory.isNotBlank() && email.isNotBlank() && tel.isNotBlank() && body.isNotBlank()
                    && contactName.isNotBlank() && contactBuildingName.isNotBlank() && contactBuildingAddress.isNotBlank()
                    && contactRoomNumber.isNotBlank() && selectPrefectures.isNotBlank() && selectAge.isNotBlank() && selectProfession.isNotBlank()
    }

    fun postInquiry() {
        viewModelScope.launch {
            val response = contactRepository.postInquiry(
                RequestInquiryForm(
                    kind = categories.value.first { it.title == selectCategory }.slug,
                    email = email,
                    tel = tel,
                    body = "お名前：${contactName}\n" +
                            "物件名：${contactBuildingName}\n" +
                            "部屋番号：${contactRoomNumber}\n" +
                            "物件所在地：${contactBuildingAddress}\n" +
                            "ご連絡先メールアドレス：${email}\n" +
                            "お電話番号：${tel}\n" +
                            "都道府県：${selectPrefectures}\n" +
                            "ご年齢：${selectAge}\n" +
                            "性別：${selectSex}\n" +
                            "ご職業：${selectProfession}\n\n" +
                            "お問い合わせ内容：\n${body}\n",
                    name = contactName,
                    residence_name = contactBuildingName,
                    room_number = contactRoomNumber,
                    office_name = getOfficeName(selectPrefectures),
                )
            )
            if (response.status == Status.SUCCESS) {
                _apiState.value = ApiState.Success
            } else {
                _apiState.value = ApiState.Empty
                _appError.value = response.appError
            }
        }
    }
}