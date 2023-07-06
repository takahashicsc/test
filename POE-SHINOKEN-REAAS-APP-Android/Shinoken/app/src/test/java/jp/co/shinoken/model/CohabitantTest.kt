package jp.co.shinoken.model

import jp.co.shinoken.model.api.Contact
import jp.co.shinoken.model.api.Reside
import jp.co.shinoken.model.api.Resident
import jp.co.shinoken.model.api.RoommateRequest
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CohabitantTest {
    private val mockReside = Reside(
        resident = Resident(
            id = 0,
            name = "Test Example",
            nameKana = "test example",
            residenceName = "aaaaa",
            authId = "Resident_15",
            code = "e6af23fe",
            birthday = "1970-12-21",
            contacts = listOf(
                Contact(
                    kind = Contact.Kind.Tel,
                    value = "000-0000-0000+041fac994a3ddd7a"
                ),
                Contact(
                    kind = Contact.Kind.Email,
                    value = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp"
                )
            ),
            myself = true
        ),
        subresident = Resident(
            id = 0,
            authId = "Subresident_15",
            code = "e6af23fe",
            name = "Test Example2",
            nameKana = "test example",
            residenceName = "aaaaa",
            birthday = "1970-12-21",
            contacts = listOf(
                Contact(
                    kind = Contact.Kind.Tel,
                    value = "000-0000-0000+041fac994a3ddd7a"
                ),
                Contact(
                    kind = Contact.Kind.Email,
                    value = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp"
                )
            ),
            myself = false
        ),
        roommates = listOf(
            Resident(
                id = 0,
                authId = "Subresident_15",
                code = "e6af23fe",
                name = "Test Example3",
                nameKana = "test example",
                residenceName = "aaaaa",
                birthday = "1970-12-21",
                contacts = listOf(
                    Contact(
                        kind = Contact.Kind.Tel,
                        value = "000-0000-0000+041fac994a3ddd7a"
                    ),
                    Contact(
                        kind = Contact.Kind.Email,
                        value = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp"
                    )
                ),
                myself = false
            )
        ),
        roommateRequests = listOf(
            RoommateRequest(
                id = 7,
                code = "",
                name = "同居人A",
                birthday = "2000-10-10",
                email = "test+50911d2b@example.com",
                tel = null,
                status = RoommateRequest.Status.Pending,
            )
        )
    )

    @Test
    fun chargeDetailConvertApiResponse() {
        val cohabitants = CohabitantParent.convertApiResponse(mockReside)
        assertEquals(
            listOf(
                CohabitantParent(
                    resideType = ResideType.Reside,
                    cohabitants = listOf(
                        Cohabitant(
                            id = 0,
                            name = "Test Example",
                            code = "",
                            status = Cohabitant.CohabitantStatus.Approved,
                            email = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
                            birthday = "1970-12-21"
                        )
                    )
                ),
                CohabitantParent(
                    resideType = ResideType.Cohabitant,
                    cohabitants = listOf(
                        Cohabitant(
                            id = 0,
                            name = "Test Example3",
                            code = "",
                            status = Cohabitant.CohabitantStatus.Approved,
                            email = "rv-blackhole+041fac994a3ddd7a@reivo.co.jp",
                            birthday = "1970-12-21"
                        ),
                        Cohabitant(
                            id = 7,
                            name = "同居人A",
                            code = "",
                            status = Cohabitant.CohabitantStatus.Pending,
                            email = "test+50911d2b@example.com",
                            birthday = "2000-10-10"
                        )
                    )
                )
            ),
            cohabitants
        )
    }
}