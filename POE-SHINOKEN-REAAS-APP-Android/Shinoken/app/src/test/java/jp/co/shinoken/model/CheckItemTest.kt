package jp.co.shinoken.model

import jp.co.shinoken.model.api.CheckForm
import jp.co.shinoken.model.api.CheckForms
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.*

class CheckItemTest {
    private val mockCheckForms =
        CheckForms(
            data = listOf(
                CheckForm(
                    slug = "a",
                    category = "opening",
                    title = "玄関ドア",
                    result = null,
                    checks = listOf(CheckForm.Check(point = "", by = "")),
                    description = "",
                    images = listOf()
                ),
                CheckForm(
                    slug = "b",
                    category = "kitchen-kitchen",
                    title = "インターホン",
                    result = null, checks = listOf(CheckForm.Check(point = "", by = "")),
                    description = "",
                    images = listOf()
                ),
            ),
            categories = listOf(
                CheckForms.Category(
                    slug = "opening",
                    title = "玄関・廊下"
                ),
                CheckForms.Category(
                    slug = "kitchen-kitchen",
                    title = "キッチン"
                ),
                CheckForms.Category(
                    slug = "washing",
                    title = "脱衣"
                )
            ),
            submittable = true,
            deadline = "",
        )

    @Test
    fun chargesConvertApiResponse() {
        val checkCategory = CheckCategory.convertApiResponse(mockCheckForms)

        assertEquals(
            listOf(
                CheckCategory(
                    name = "玄関・廊下",
                    checkItems = listOf(
                        CheckItem(
                            slug = "a",
                            category = "opening",
                            title = "玄関ドア",
                            id = UUID(1111, 1111),
                            result = null,
                            description = "",
                            images = listOf(),
                            checks = listOf()
                        )
                    )
                ),
                CheckCategory(
                    name = "キッチン", checkItems = listOf(
                        CheckItem(
                            slug = "b",
                            category = "kitchen-kitchen",
                            id = UUID(2222, 2222),
                            title = "インターホン",
                            result = null,
                            description = "",
                            images = listOf(),
                            checks = listOf()
                        )
                    )
                ),
            ),
            listOf(
                CheckCategory(
                    name = checkCategory[0].name,
                    checkItems = listOf(
                        CheckItem(
                            id = UUID(1111, 1111),
                            slug = checkCategory[0].checkItems?.get(0)!!.slug,
                            category = checkCategory[0].checkItems?.get(0)!!.category,
                            title = checkCategory[0].checkItems?.get(0)!!.title,
                            result = null,
                            description = checkCategory[0].checkItems?.get(0)!!.description,
                            images = listOf(),
                            checks = listOf()
                        )
                    )
                ),
                CheckCategory(
                    name = checkCategory[1].name,
                    checkItems = listOf(
                        CheckItem(
                            id = UUID(2222, 2222),
                            slug = checkCategory[1].checkItems?.get(0)!!.slug,
                            category = checkCategory[1].checkItems?.get(0)!!.category,
                            title = checkCategory[1].checkItems?.get(0)!!.title,
                            result = null,
                            description = checkCategory[1].checkItems?.get(0)!!.description,
                            images = listOf(),
                            checks = listOf()
                        )
                    )
                )
            )
        )
    }
}