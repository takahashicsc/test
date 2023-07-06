package jp.co.shinoken.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets

class BillsTest {
    private lateinit var bills: Bills

    @Before
    fun setUp() {
        val jsonString =
            IOUtils.toString(
                javaClass.getResourceAsStream("bills.json"),
                StandardCharsets.UTF_8
            )
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        bills = moshi.adapter(Bills::class.java).fromJson(jsonString)!!
    }

    @Test
    fun getBills() {
        assertEquals(3, bills.total)

        val bill = bills.data.first()
        assertEquals("202102", bill.month)
        assertEquals(97640, bill.totalValue)
        assertEquals(5, bill.items.size)

        val item = bill.items.first()

        assertEquals(Bill.Item.Slug.Electricity, item.slug)
        assertEquals(3690, item.value)
        assertEquals(Bill.Item.Category.Electricity, item.category)
    }
}