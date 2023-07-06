package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ShinokenBootApiService
import jp.co.shinoken.repository.AccountRepository
import javax.inject.Inject

class DataAccountRepository @Inject constructor(private val shinokenBootApiService: ShinokenBootApiService) : AccountRepository {
    override fun getAccounts(): List<String> {
        return listOf("ハーモニーテラス 堺 203号室","ハーモニーテラス 堺 204号室","ハーモニーテラス 堺 205号室")
    }
}