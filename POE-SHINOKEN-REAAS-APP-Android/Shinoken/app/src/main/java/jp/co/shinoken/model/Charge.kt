package jp.co.shinoken.model

import jp.co.shinoken.extension.convertDateToString
import jp.co.shinoken.extension.convertStringToDate
import jp.co.shinoken.model.api.Bill
import jp.co.shinoken.model.api.Bills
import java.io.Serializable

data class Charges(
    val items: List<List<Charge>>,
    val lastUpdatedAt: String?,
) : Serializable {
    companion object {
        fun convertApiResponse(bills: Bills): Charges {
            return Charges(
                lastUpdatedAt = bills.lastUpdatedAt,
                items =
                bills.data.map {
                    it.items
                        .groupBy {
                            it.category
                        }.map { group ->
                            Charge(
                                name = group.value.map { it.title }.first(),
                                chargesType = ChargesType.convertChargesTypeFromCategory(group.key),
                                charge = group.value.map { it.value }.sum(),
                                chargeDate = it.month
                            )
                        }
                        .sortedWith { from, to -> from.chargesType.sortedNumber() - to.chargesType.sortedNumber() }
                }.flatten().groupBy { it.chargesType }.map { it.value }

            )
        }
    }

    fun getLastUpdatedAtFormatString(): String? {
        lastUpdatedAt ?: return null
        val lastUpdatedAtDate = lastUpdatedAt.convertStringToDate()
        return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd")
    }
}

data class ChargesLatest(
    val total: Int,
    val items: List<Charge>,
    val updatedAt: String,
) : Serializable {
    companion object {
        fun convertApiResponse(data: Bill): ChargesLatest {
            return ChargesLatest(
                total = data.totalValue,
                items = data.items.groupBy { it.slug }.map { group ->
                    Charge(
                        name = group.value.map { it.title }.first(),
                        chargesType = ChargesType.convertChargesTypeFromSlug(group.key),
                        charge = group.value.map { it.value }.sum(),
                        chargeDate = data.month,
                    )
                },
                updatedAt = data.updatedAt
            )
        }
    }

    fun getUpdatedAtFormatString(): String? {
        updatedAt
        val lastUpdatedAtDate = updatedAt.convertStringToDate()
        return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd")
    }

    fun getSectionAtFormatString(): Pair<String, String>? {
        if (items.isEmpty()) return null

        val sectionYear = items.first().chargeDate.substring(0, 4)
        val sectionMonth = items.first().chargeDate.substring(4, 6)
        return Pair(sectionYear, sectionMonth)
    }
}

data class ChargeDetail(
    val total: Int,
    val items: List<Charge>,
    val updatedAt: String,
) : Serializable {
    companion object {
        fun convertApiResponse(data: Bill): ChargeDetail {
            val others = data.items.filter { it.category == Bill.Item.Category.Other }
            return ChargeDetail(
                total = others.map { it.value }.sum(),
                items = others.groupBy { it.slug }.map { group ->
                    Charge(
                        name = group.value.map { it.title }.first(),
                        chargesType = ChargesType.convertChargesTypeFromSlug(group.key),
                        charge = group.value.map { it.value }.sum(),
                        chargeDate = data.month,
                    )
                },
                updatedAt = data.updatedAt
            )
        }
    }

    fun getUpdatedAtFormatString(): String? {
        updatedAt
        val lastUpdatedAtDate = updatedAt.convertStringToDate()
        return lastUpdatedAtDate?.convertDateToString("yyyy.MM.dd")
    }

    fun getSectionAtFormatString(): Pair<String, String>? {
        if (items.isEmpty()) return null

        val sectionYear = items.first().chargeDate.substring(0, 4)
        val sectionMonth = items.first().chargeDate.substring(4, 6)
        return Pair(sectionYear, sectionMonth)
    }
}

data class Charge(
    val name: String,
    val chargesType: ChargesType,
    val charge: Int,
    val chargeDate: String,
) : Serializable {
    fun getChargeDateFormatString(): Pair<String, String>? {
        val sectionYear = chargeDate.substring(0, 4)
        val sectionMonth = chargeDate.substring(4, 6)
        return Pair(sectionYear, sectionMonth)
    }
}