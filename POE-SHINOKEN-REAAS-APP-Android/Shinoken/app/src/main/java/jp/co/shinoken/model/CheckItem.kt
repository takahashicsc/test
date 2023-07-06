package jp.co.shinoken.model

import com.squareup.moshi.Json
import jp.co.shinoken.model.api.CheckForm
import jp.co.shinoken.model.api.CheckForms
import jp.co.shinoken.model.api.Icon
import jp.co.shinoken.model.api.Image
import java.io.Serializable
import java.util.*

data class CheckCategory(
    val name: String,
    val checkItems: List<CheckItem>
) : Serializable {
    companion object {
        fun convertApiResponse(checkForms: CheckForms): List<CheckCategory> {
            val checkItems = checkForms.data.map {
                CheckItem(
                    slug = it.slug,
                    category = it.category,
                    title = it.title,
                    result = it.result,
                    description = it.description,
                    images = it.images.map { it },
                    checks = it.checks
                )
            }
            return checkForms.categories.map { category ->
                CheckCategory(
                    name = category.title,
                    checkItems = checkItems.filter { it.category == category.slug }
                )
            }.filter {
                it.checkItems.isNotEmpty()
            }
        }
    }
}

data class CheckItem(
    var slug: String,
    var id: UUID = UUID.randomUUID(),
    var category: String,
    var title: String,
    var result: CheckFormResult?,
    var description: String?,
    var images: List<Image>,
    val checks: List<CheckForm.Check>
) : Serializable {
    enum class Category : Serializable {
        @Json(name = "opening")
        Opening
    }
}