package jp.co.shinoken.activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.shinoken.BuildConfig
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.api.VersionCheck
import jp.co.shinoken.repository.AuthRepository
import jp.co.shinoken.repository.StoreRepository
import jp.co.shinoken.repository.VersionCheckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LauncherViewModel @ViewModelInject constructor(
    private val versionCheckRepository: VersionCheckRepository,
    private val authRepository: AuthRepository,
    private val storeRepository: StoreRepository
) :
    ViewModel() {
    val launcherNavigation: StateFlow<LauncherNavigation?> get() = _launcherNavigation
    private val _launcherNavigation = MutableStateFlow<LauncherNavigation?>(null)

    init {
        fetch()
    }

    fun initUUID() {
        if (storeRepository.getUUID().isNullOrEmpty().not()) return
        storeRepository.saveUUID()
    }

    fun fetch() {
        viewModelScope.launch {
            try {
                val response = versionCheckRepository.getVersionCheck()

                if (response.isSuccessful) {
                    _launcherNavigation.value =
                        if (response.body() is VersionCheck && response.body()!!.code == "app_version_update_required"
                            && !BuildConfig.DEBUG) {
                            LauncherNavigation.Update
                        } else {
                            fetchAuthSession()
                        }
                    return@launch
                } else if (response.code() == 426){
                    _launcherNavigation.value = LauncherNavigation.Update
                    return@launch
                }
            } catch (e: Exception) {
                _launcherNavigation.value = fetchAuthSession()
            }
        }
    }


    suspend fun fetchAuthSession(): LauncherNavigation {
        val response = authRepository.fetchAuthSession()
        return if (response.status == Status.SUCCESS && response.data!! && storeRepository.getApiToken()
                .isNullOrEmpty().not()
        ) {
            LauncherNavigation.Login
        } else {
            authRepository.signOut()
            LauncherNavigation.WalkThrough
        }
    }

    enum class LauncherNavigation {
        WalkThrough,
        Login,
        Update
    }
}