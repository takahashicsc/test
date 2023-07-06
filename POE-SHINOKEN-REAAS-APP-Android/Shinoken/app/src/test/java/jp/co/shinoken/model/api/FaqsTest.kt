package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.shinoken.model.FaqCategory
import jp.co.shinoken.model.Faqs
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class FaqsTest {
    private lateinit var faqs: Faqs

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("faqs.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        faqs = moshi.adapter(Faqs::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getFaqs() {
        assertEquals(9, faqs.total)
        assertEquals(
            listOf(
                FaqCategory(
                    id = 1,
                    name = "設備",
                    path = "/faq/cat/facility",
                    image = null,
                    status = Status.Opened,
                    serialCode = 0,
                    isSelected = false
                ),
                FaqCategory(
                    id = 2, name = "契約関連", path = "/faq/cat/contract",
                    image = null,
                    status = Status.Opened,
                    serialCode = 0,
                    isSelected = false
                ),
                FaqCategory(
                    id = 3, name = "家賃", path = "/faq/cat/rent",
                    image = null,
                    status = Status.Opened,
                    serialCode = 0,
                    isSelected = false
                )
            ),
            faqs.categories
        )

        val faq = faqs.data.first()
        assertEquals(58, faq.id)
        assertEquals("共用部に蜂の巣があります。", faq.title)
        assertEquals("450c9af6cea90e91", faq.slug)
        assertEquals("ガスメーターの安全装置がはたらき、ガスの供給が...", faq.contentText)
        assertEquals(listOf<Image>(), faq.images)
        assertEquals(
            listOf<FaqCategory>(
                FaqCategory(
                    id = 1,
                    name = "設備",
                    path = "/faq/cat/facility",
                    image = null,
                    status = Status.Opened,
                    serialCode = 0,
                    isSelected = false
                )
            ),
            faq.categories
        )
        assertEquals(
            listOf(
                Link(
                    kind = Kind.DeepLink,
                    title = "お客様のマイページ",
                    url = "shinoken-residentapp-demo:///mypage"
                )
            ),
            faq.links
        )
    }
}
