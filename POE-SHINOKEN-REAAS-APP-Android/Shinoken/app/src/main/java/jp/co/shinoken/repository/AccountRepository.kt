package jp.co.shinoken.repository

interface AccountRepository {
    fun getAccounts(): List<String>
}