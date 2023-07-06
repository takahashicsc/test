package jp.co.shinoken.repository

interface PasswordResetRepository {
    fun postPasswordReset()
    fun postPasswordResetForm()
}