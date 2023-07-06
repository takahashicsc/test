package jp.co.shinoken.repository.impl

import jp.co.shinoken.model.Manual
import jp.co.shinoken.repository.ManualRepository
import javax.inject.Inject

class DataManualRepository @Inject constructor() : ManualRepository {
    override fun getManuals(): List<Manual> {
        return listOf(
            Manual("住まいのマニュアル_ハーモニーテラス.pdf", ""),
            Manual("シーリングライト.pdf", "")
        )
    }
}