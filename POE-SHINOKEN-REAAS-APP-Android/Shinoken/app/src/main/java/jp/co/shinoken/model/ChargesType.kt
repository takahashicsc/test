package jp.co.shinoken.model

import jp.co.shinoken.model.api.Bill
import java.io.Serializable

enum class ChargesType : Serializable {
    Electricity,
    Gas,
    Tap,
    HouseRent,
    Administrative,
    Other;

    fun sortedNumber(): Int {
        return when (this) {
            Electricity -> 1
            Gas -> 2
            else -> 0
        }
    }

    companion object {
        fun convertChargesTypeFromCategory(category: Bill.Item.Category): ChargesType {
            return when (category) {
                Bill.Item.Category.Gas -> {
                    Gas
                }
                Bill.Item.Category.Electricity -> {
                    Electricity
                }
                else -> {
                    Other
                }
            }
        }

        fun convertChargesTypeFromSlug(slug: String): ChargesType {
            return when (slug) {
                "rent"-> {
                    HouseRent
                }
                "electricity" -> {
                    Electricity
                }
                "water" -> {
                    Tap
                }
                "gas" -> {
                    Gas
                }
                "administration" -> {
                    Administrative
                }
                else -> {
                    Other
                }
            }
        }
    }
}