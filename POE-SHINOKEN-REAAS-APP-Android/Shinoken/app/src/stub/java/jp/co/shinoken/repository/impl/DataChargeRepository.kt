package jp.co.shinoken.repository.impl

import jp.co.shinoken.model.Charge
import jp.co.shinoken.model.ChargeDetail
import jp.co.shinoken.model.ChargesType
import jp.co.shinoken.repository.ChargeRepository
import javax.inject.Inject

class DataChargeRepository @Inject constructor() : ChargeRepository {
    private val chargesElectricity = listOf(
        Charge(
            chargesType = ChargesType.Electricity,
            charge = 10810,
            chargeDate = "2020.10.10"
        ),
        Charge(
            chargesType = ChargesType.Electricity,
            charge = 10810,
            chargeDate = "2020.11.10"
        ), Charge(
            chargesType = ChargesType.Electricity,
            charge = 10810,
            chargeDate = "2020.12.10"
        ), Charge(
            chargesType = ChargesType.Electricity,
            charge = 10810,
            chargeDate = "2021.01.10"
        )
    )

    private val chargesGas = listOf(
        Charge(
            chargesType = ChargesType.Gas,
            charge = 5555,
            chargeDate = "2020.10.11"
        ),
        Charge(
            chargesType = ChargesType.Gas,
            charge = 5555,
            chargeDate = "2020.11.11"
        ), Charge(
            chargesType = ChargesType.Gas,
            charge = 5555,
            chargeDate = "2020.12.11"
        ), Charge(
            chargesType = ChargesType.Gas,
            charge = 5555,
            chargeDate = "2021.01.11"
        )
    )

    private val chargesTap = listOf(
        Charge(
            chargesType = ChargesType.Tap,
            charge = 5555,
            chargeDate = "2020.10.11"
        ),
        Charge(
            chargesType = ChargesType.Tap,
            charge = 5555,
            chargeDate = "2020.11.11"
        ), Charge(
            chargesType = ChargesType.Tap,
            charge = 5555,
            chargeDate = "2020.12.11"
        ), Charge(
            chargesType = ChargesType.Tap,
            charge = 5555,
            chargeDate = "2021.01.11"
        )
    )

    private val chargesHouseRent = listOf(
        Charge(
            chargesType = ChargesType.HouseRent,
            charge = 120231,
            chargeDate = "2020.10.25"
        ),
        Charge(
            chargesType = ChargesType.HouseRent,
            charge = 120231,
            chargeDate = "2020.11.25"
        ), Charge(
            chargesType = ChargesType.HouseRent,
            charge = 120231,
            chargeDate = "2020.12.25"
        ), Charge(
            chargesType = ChargesType.HouseRent,
            charge = 120231,
            chargeDate = "2021.01.25"
        )
    )

    override fun getCharges(chargesType: ChargesType): List<Charge> {
        return when (chargesType) {
            ChargesType.Electricity -> chargesElectricity
            ChargesType.Gas -> chargesGas
            ChargesType.Tap -> chargesTap
            ChargesType.HouseRent -> chargesHouseRent
        }
    }

    override fun getChargeDetail(id: Int): ChargeDetail {
        val charge = listOf(
            ChargeDetail(1),
            ChargeDetail(2)
        )
        return charge.find { it.id == id } ?: charge[0]
    }
}