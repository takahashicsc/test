package jp.co.shinoken.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import jp.co.shinoken.R
import jp.co.shinoken.model.api.Contact
import jp.co.shinoken.model.api.Reside
import jp.co.shinoken.model.api.RoommateRequest
import java.io.Serializable


data class Cohabitant(
    val id: Int,
    val code: String,
    val name: String?,
    val status: CohabitantStatus,
    val email: String?,
    val birthday: String?
) : Serializable {
    enum class CohabitantStatus(@DrawableRes val backgroundRes: Int, @StringRes val textRes: Int) {
        Pending(R.drawable.bg_status_pending, R.string.cohabitant_status_pending),
        Approved(R.drawable.bg_status_approve, R.string.cohabitant_status_approve),
        Rejected(R.drawable.bg_status_reject, R.string.cohabitant_status_reject),
    }
}

enum class ResideType(@StringRes val nameRes: Int) {
    Reside(R.string.reside),
    Cohabitant(R.string.cohabitant)
}

data class CohabitantParent(
    val resideType: ResideType,
    val cohabitants: List<Cohabitant>
) : Serializable {
    companion object {
        fun convertApiResponse(reside: Reside): List<CohabitantParent> {
            val resides = mutableListOf(
                Cohabitant(
                    id = reside.resident.id,
                    code = reside.resident.code,
                    name = reside.resident.name,
                    status = Cohabitant.CohabitantStatus.Approved,
                    email = reside.resident.contacts.first { it.kind == Contact.Kind.Email }.value,
                    birthday = reside.resident.birthday
                ),
            )

            if (reside.subresident != null && reside.subresident.id != reside.resident.id) {
                resides.add(
                    Cohabitant(
                        id = reside.subresident.id,
                        code = reside.resident.code,
                        name = reside.subresident.name,
                        status = Cohabitant.CohabitantStatus.Approved,
                        email = reside.subresident.contacts.first { it.kind == Contact.Kind.Email }.value,
                        birthday = reside.subresident.birthday
                    )
                )
            }

            val roommateRequest =
                reside.roommateRequests.map {
                    val status = when (it.status) {
                        RoommateRequest.Status.Approved -> Cohabitant.CohabitantStatus.Approved
                        RoommateRequest.Status.Pending -> Cohabitant.CohabitantStatus.Pending
                        RoommateRequest.Status.Rejected -> Cohabitant.CohabitantStatus.Rejected
                    }
                    return@map Cohabitant(
                        id = it.id,
                        code = it.code,
                        name = it.name,
                        status = status,
                        email = it.email,
                        birthday = it.birthday
                    )
                }

            val cohabitants =
                reside.roommates.map {
                    Cohabitant(
                        id = it.id,
                        code = it.code,
                        name = it.name,
                        status = Cohabitant.CohabitantStatus.Approved,
                        email = it.contacts.first { contact -> contact.kind == Contact.Kind.Email }.value,
                        birthday = it.birthday
                    )
                }.filter { cohabitants ->
                    val conflictCohabitant: Cohabitant? =
                        roommateRequest.firstOrNull { roommateRequest ->
                            roommateRequest.id == cohabitants.id
                        }
                    conflictCohabitant == null
                }

            return listOf(
                CohabitantParent(
                    resideType = ResideType.Reside,
                    cohabitants = resides.toList()
                ),
                CohabitantParent(
                    resideType = ResideType.Cohabitant,
                    cohabitants = cohabitants.plus(roommateRequest)
                )
            )
        }
    }
}