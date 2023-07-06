package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class BoardTest {
    private lateinit var boards: Boards

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("boards.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        boards = moshi.adapter(Boards::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getBoards() {
        assertEquals(24, boards.total)

        val board = boards.data.first()
        assertEquals(52, board.id)
        assertEquals("掲示板テスト記事board9e1cfd69", board.title)
        assertEquals("2021-03-22T13:42:20.000Z", board.startAt)
        assertEquals("board9e1cfd69", board.slug)
        assertEquals(
            "<p>○月○日町内会行事中止のお知らせ 毎年開催してきた本行事は、新型コロナウイルス感染拡大防止のため、今年度は残念ながら中止とさせていただきます。</p>",
            board.contentText
        )
        assertEquals("2021.03.22", board.getStartAtFormatString())
    }
}