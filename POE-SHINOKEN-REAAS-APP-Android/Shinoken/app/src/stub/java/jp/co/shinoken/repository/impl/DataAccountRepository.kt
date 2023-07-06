package jp.co.shinoken.repository.impl

import jp.co.shinoken.repository.AccountRepository
import javax.inject.Inject

class DataAccountRepository @Inject constructor() : AccountRepository {
    override fun getAccounts(): List<String> {
        return listOf("ハーモニーテラス 堺 203号室","ハーモニーテラス 堺 204号室","ハーモニーテラス 堺 205号室")
    }
}