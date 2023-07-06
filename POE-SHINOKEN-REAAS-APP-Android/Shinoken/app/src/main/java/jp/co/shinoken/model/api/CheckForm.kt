package jp.co.shinoken.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import jp.co.shinoken.model.CheckFormResult
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CheckForms(
    val data: List<CheckForm>,
    @Json(name = "submittable")
    val submittable: Boolean,
    @Json(name = "deadline")
    val deadline: String,
    val categories: List<Category>
) {
    @JsonClass(generateAdapter = true)
    data class Category(
        val slug: String,
        val title: String,
    )

    fun getDeadlineFormatString(): String? {
        deadline ?: return null
        val deadlineDate = deadline.convertStringToDate()
        return deadlineDate?.convertDateToString("yyyy.MM.dd")
    }
}

@JsonClass(generateAdapter = true)
data class CheckFormDetail(
    val data: CheckForm
)

@JsonClass(generateAdapter = true)
data class ResponseSuccessCheckForm(
    val message: String,
    val data: CheckForm
)

@JsonClass(generateAdapter = true)
data class CheckForm(
    val slug: String,
    val category: String,
    val title: String,
    val checks: List<Check>,
    val result: CheckFormResult?,
    val description: String?,
    val images: List<Image>
) {
    @JsonClass(generateAdapter = true)
    data class Check(
        val point: String,
        val by: String
    ) : Serializable
}