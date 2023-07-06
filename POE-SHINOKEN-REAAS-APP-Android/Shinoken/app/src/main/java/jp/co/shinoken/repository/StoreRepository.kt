package jp.co.shinoken.repository

interface StoreRepository {
    fun getCurrentAccountId(): String?
    fun saveCurrentAccountId(id: String)
    fun getApiToken(): String?
    fun saveApiToken(token: String)
    fun getApiRefreshToken(): String?
    fun saveRefreshToken(token: String)
    fun getUUID(): String?
    fun saveUUID()
    fun deleteApiTokens()
    fun isWalkThrough(): Boolean
    fun saveWalkThrough()
    fun savePushToken(pushToken: String, isPost: Boolean)
    fun getPushToken(): String?
    fun isPostPushToken(): Boolean
}