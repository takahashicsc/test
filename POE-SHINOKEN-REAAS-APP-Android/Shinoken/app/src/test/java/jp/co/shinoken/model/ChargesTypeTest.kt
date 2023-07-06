package jp.co.shinoken.model

import jp.co.shinoken.model.api.Bill
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChargesTypeTest {
    @Test
    fun convertChargesTypeFromCategory() {
        assertEquals(
            ChargesType.Electricity,
            ChargesType.convertChargesTypeFromCategory(Bill.Item.Category.Electricity)
        )

        assertEquals(
            ChargesType.Gas,
            ChargesType.convertChargesTypeFromCategory(Bill.Item.Category.Gas)
        )

        assertEquals(
            ChargesType.Other,
            ChargesType.convertChargesTypeFromCategory(Bill.Item.Category.Other)
        )
    }

    @Test
    fun convertChargesTypeFromSlug() {
        assertEquals(
            ChargesType.Electricity,
            ChargesType.convertChargesTypeFromSlug( "electricity",)
        )

        assertEquals(
            ChargesType.Gas,
            ChargesType.convertChargesTypeFromSlug("gas")
        )

        assertEquals(
            ChargesType.HouseRent,
            ChargesType.convertChargesTypeFromSlug("rent")
        )

        assertEquals(
            ChargesType.Tap,
            ChargesType.convertChargesTypeFromSlug("water")
        )

        assertEquals(
            ChargesType.Administrative,
            ChargesType.convertChargesTypeFromSlug("administration")
        )
    }
}