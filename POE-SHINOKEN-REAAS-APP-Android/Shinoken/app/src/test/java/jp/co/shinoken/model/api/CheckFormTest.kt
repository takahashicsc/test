package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.shinoken.model.CheckFormResult
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class CheckFormTest {
    private lateinit var checkForms: CheckForms

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("check_form.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        checkForms = moshi.adapter(CheckForms::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getCheckForms() {
        assertEquals(9, checkForms.data.size)
        assertEquals(false, checkForms.submittable)
        assertEquals("2021-05-02T01:12:31.240Z", checkForms.deadline)
        assertEquals(
            listOf(
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
            checkForms.categories
        )

        val checkForm = checkForms.data.first()
        assertEquals("opening-door", checkForm.slug)
        assertEquals("opening", checkForm.category)
        assertEquals("玄関ドア", checkForm.title)
        assertEquals(
            listOf(
                CheckForm.Check(
                    point = "クローザーの速度・施錠の作動・U字ロック・開閉ガタツキ",
                    by = "目視・動作"
                ),
                CheckForm.Check(
                    point = "ドア本体及び枠のよごれ・傷、郵便受け設置状態",
                    by = "目視"
                )
            ),
            checkForm.checks
        )
        assertEquals(CheckFormResult.OK, checkForm.result)
        assertEquals(null, checkForm.description)
        assertEquals(listOf<Image>(), checkForm.images)
    }
}