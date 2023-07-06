package jp.co.shinoken.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.co.shinoken.model.DataKeyConstants
import jp.co.shinoken.model.StubConstants
import jp.co.shinoken.repository.StoreRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : StoreRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataKeyConstants.DataStoreName)
    override fun getCurrentAccountId(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[DataKeyConstants.CurrentAccountId] ?: ""
            }
    }

    override suspend fun saveCurrentAccountId(id: String) {
        context.dataStore.edit { settings ->
            settings[DataKeyConstants.CurrentAccountId] = id
        }
    }

    override fun getApiToken(): Flow<String> {
        return flowOf(StubConstants.apiToken)
    }

    override suspend fun saveApiToken(token: String) {
    }

    override fun getApiRefreshToken(): Flow<String> {
        return flowOf(StubConstants.apiToken)
    }

    override suspend fun saveRefreshToken(token: String) {
    }
}