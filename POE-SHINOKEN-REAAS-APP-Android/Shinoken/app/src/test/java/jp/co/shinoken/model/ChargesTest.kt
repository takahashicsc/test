package jp.co.shinoken.model

import jp.co.shinoken.model.api.Bill
import jp.co.shinoken.model.api.Bills
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ChargesTest {
    private val mockBills = Bills(
        total = 10000,
        latest = Bill(
            month = "20201010",
            totalValue = 500,
            items = listOf(
                Bill.Item(
                    title = "電気代",
                    slug = "electricity",
                    value = 1000,
                    category = Bill.Item.Category.Electricity
                ),
                Bill.Item(
                    title = "ガス代",
                    slug = "gas",
                    value = 1000,
                    category = Bill.Item.Category.Gas
                ),
                Bill.Item(
                    title = "家賃",
                    slug = "rent",
                    value = 10000,
                    category = Bill.Item.Category.Other
                ),
                Bill.Item(
                    title = "電気代",
                    slug = "electricity",
                    value = 1000,
                    category = Bill.Item.Category.Electricity
                ),
            ),
            updatedAt = "2021-04-07T12:10:02.429Z"
        ),
        data = listOf(
            Bill(
                month = "20201010",
                totalValue = 10000,
                items = listOf(
                    Bill.Item(
                        title = "電気代",
                        slug =  "electricity",
                        value = 1000,
                        category = Bill.Item.Category.Electricity
                    ),
                    Bill.Item(
                        title = "ガス代",
                        slug = "gas",
                        value = 1000,
                        category = Bill.Item.Category.Gas
                    ),
                    Bill.Item(
                        title = "家賃",
                        slug = "rent",
                        value = 10000,
                        category = Bill.Item.Category.Other
                    ),
                ),
                updatedAt = "2021-04-07T12:10:02.429Z"
            ),
            Bill(
                month = "20200910",
                totalValue = 10000,
                items = listOf(
                    Bill.Item(
                        title = "電気代",
                        slug =  "electricity",
                        value = 100,
                        category = Bill.Item.Category.Electricity
                    ),
                    Bill.Item(
                        title = "ガス代",
                        slug = "gas",
                        value = 100,
                        category = Bill.Item.Category.Gas
                    ),
                    Bill.Item(
                        title = "家賃",
                        slug = "rent",
                        value = 20000,
                        category = Bill.Item.Category.Other
                    ),
                ),
                updatedAt = "2021-04-07T12:10:02.429Z"
            ),
        ),
        lastUpdatedAt = "2021-04-07T12:10:02.429Z"
    )

    @Test
    fun chargesConvertApiResponse() {
        val charges = Charges.convertApiResponse(mockBills)

        assertEquals(
            Charges(
                items = listOf(
                    listOf(
                        Charge(
                            name = "家賃",
                            chargesType = ChargesType.Other,
                            charge = 10000,
                            chargeDate = "20201010"
                        ), Charge(
                            name = "家賃",
                            chargesType = ChargesType.Other,
                            charge = 20000,
                            chargeDate = "20200910"
                        )
                    ),
                    listOf(
                        Charge(
                            name = "電気代",
                            chargesType = ChargesType.Electricity,
                            charge = 1000,
                            chargeDate = "20201010"
                        ),
                        Charge(
                            name = "電気代",
                            chargesType = ChargesType.Electricity,
                            charge = 100,
                            chargeDate = "20200910"
                        ),
                    ), listOf(
                        Charge(
                            name = "ガス代",
                            chargesType = ChargesType.Gas,
                            charge = 1000,
                            chargeDate = "20201010"
                        ),
                        Charge(
                            name = "ガス代",
                            chargesType = ChargesType.Gas,
                            charge = 100,
                            chargeDate = "20200910"
                        )
                    )
                ),
                "2021-04-07T12:10:02.429Z"
            ),
            charges
        )
    }

    @Test
    fun getLastUpdatedAtFormatString() {
        val charges = Charges.convertApiResponse(mockBills)
        assertEquals(
            "2021.04.07",
            charges.getLastUpdatedAtFormatString()
        )
    }

    @Test
    fun chargeLatestConvertApiResponse() {
        val chargeDetail = ChargesLatest.convertApiResponse(mockBills.latest!!)
        assertEquals(
            ChargesLatest(
                total = 500,
                items = listOf(
                    Charge(
                        name = "電気代",
                        chargesType = ChargesType.Electricity,
                        charge = 2000,
                        chargeDate = "20201010"
                    ),
                    Charge(
                        name = "ガス代",
                        chargesType = ChargesType.Gas,
                        charge = 1000,
                        chargeDate = "20201010"
                    ),
                    Charge(
                        name = "家賃",
                        chargesType = ChargesType.HouseRent,
                        charge = 10000,
                        chargeDate = "20201010"
                    )
                ),
                "2021-04-07T12:10:02.429Z"
            ),
            chargeDetail
        )
    }

    @Test
    fun chargeDetailConvertApiResponse() {
        val chargeDetail = ChargeDetail.convertApiResponse(mockBills.latest!!)
        assertEquals(
            ChargeDetail(
                total = 10000,
                items = listOf(
                    Charge(
                        name = "家賃",
                        chargesType = ChargesType.HouseRent,
                        charge = 10000,
                        chargeDate = "20201010"
                    )
                ),
                "2021-04-07T12:10:02.429Z"
            ),
            chargeDetail
        )
    }

    @Test
    fun getUpdatedAtFormatString() {
        val chargeDetail = ChargeDetail.convertApiResponse(mockBills.latest!!)
        assertEquals(
            "2021.04.07",
            chargeDetail.getUpdatedAtFormatString()
        )
    }

    @Test
    fun getSectionAtFormatString() {
        val chargeDetail = ChargeDetail.convertApiResponse(mockBills.latest!!)
        assertEquals(
            Pair("2020", "10"),
            chargeDetail.getSectionAtFormatString()
        )
    }

    @Test
    fun getChargeDateFormatString() {
        val chargeDetail = ChargeDetail.convertApiResponse(mockBills.data.first())
        assertEquals(
            Pair("2020", "10"),
            chargeDetail.items.first().getChargeDateFormatString()
        )
    }

}