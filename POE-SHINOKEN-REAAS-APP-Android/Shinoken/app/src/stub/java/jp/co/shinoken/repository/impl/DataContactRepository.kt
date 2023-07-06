package jp.co.shinoken.repository.impl

import jp.co.shinoken.model.ContactCategory
import jp.co.shinoken.repository.ContactRepository
import javax.inject.Inject

class DataContactRepository @Inject constructor() : ContactRepository {
    override fun getContactCategories(): List<ContactCategory> {
        return ContactCategory.values().toList()
    }
}