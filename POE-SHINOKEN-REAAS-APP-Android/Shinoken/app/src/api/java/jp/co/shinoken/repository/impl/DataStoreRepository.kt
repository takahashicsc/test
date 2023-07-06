package jp.co.shinoken.repository.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.co.shinoken.model.DataKeyConstants
import jp.co.shinoken.repository.StoreRepository
import java.util.*
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : StoreRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataKeyConstants.DataStoreName)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            DataKeyConstants.SharedPreferenceName,
            Context.MODE_PRIVATE
        )

    override fun getCurrentAccountId(): String? {
        return sharedPreferences.getString(DataKeyConstants.CurrentAccountId, null)
    }

    override fun saveCurrentAccountId(id: String) {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.CurrentAccountId, id)
            apply()
        }
    }

    override fun getApiToken(): String? {
        return sharedPreferences.getString(DataKeyConstants.ApiAccessToken, null)
    }

    override fun saveApiToken(token: String) {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.ApiAccessToken, token)
            apply()
        }
    }

    override fun getApiRefreshToken(): String? {
        return sharedPreferences.getString(DataKeyConstants.ApiRefreshToken, null)
    }

    override fun saveRefreshToken(token: String) {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.ApiRefreshToken, token)
            apply()
        }
    }

    override fun deleteApiTokens() {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.ApiAccessToken, null)
            putString(DataKeyConstants.ApiRefreshToken, null)
            apply()
        }
    }

    override fun getUUID(): String? {
        return sharedPreferences.getString(DataKeyConstants.AppUUID, null)
    }

    override fun saveUUID() {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.AppUUID, UUID.randomUUID().toString())
            apply()
        }
    }

    override fun isWalkThrough(): Boolean {
        return sharedPreferences.getBoolean(DataKeyConstants.AppIsWalkThrough, false)
    }

    override fun saveWalkThrough() {
        sharedPreferences.edit().run {
            putBoolean(DataKeyConstants.AppIsWalkThrough, true)
            apply()
        }
    }

    override fun savePushToken(pushToken: String, isPost: Boolean) {
        sharedPreferences.edit().run {
            putString(DataKeyConstants.PushToken, pushToken)
            putBoolean(DataKeyConstants.IsPostPushToken, isPost)
            apply()
        }
    }

    override fun getPushToken(): String? =
        sharedPreferences.getString(DataKeyConstants.PushToken, null)

    override fun isPostPushToken(): Boolean =
        sharedPreferences.getBoolean(DataKeyConstants.IsPostPushToken, false)
}