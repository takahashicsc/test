package jp.co.shinoken.api.request

import com.serjltt.moshi.adapters.SerializeNulls
import com.squareup.moshi.JsonClass
import jp.co.shinoken.model.CheckFormResult

@JsonClass(generateAdapter = true)
data class RequestCheckForm(
    @SerializeNulls var result: CheckFormResult?,
    @SerializeNulls val description: String?,
    @SerializeNulls val image1: String?,
    @SerializeNulls val image2: String?,
    @SerializeNulls val image3: String?
)