package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.CheckItem
import jp.co.shinoken.repository.CheckFormRepository
import javax.inject.Inject


class DataCheckFormRepository @Inject constructor() : CheckFormRepository {
    private val mockCheckItems = listOf(
        CheckItem(
            slug = "a",
            category = CheckItem.Category.Opening,
            title = "玄関ドア",
            result = null,
            description = "",
            images = listOf()
        ),
        CheckItem(
            slug = "b",
            category = CheckItem.Category.Opening,
            title = "インターホン",
            result = null,
            description = "",
            images = listOf()
        ),
    )

    override fun getCheckForms(): ApiResponse<List<CheckItem>> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = mockCheckItems,
            appError = null
        )

    }
}