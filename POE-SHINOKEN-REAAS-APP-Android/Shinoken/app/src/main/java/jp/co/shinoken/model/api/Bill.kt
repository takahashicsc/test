package jp.co.shinoken.model.api

import android.service.quicksettings.Tile
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
{
  "latest": {
    "month": "202102",
    "items": [
      {
        "slug": "electricity",
        "value": 3690,
        "category": "electricity",
        "updated_at": "2021-03-08T11:30:57.924Z"
      },
      {
        "slug": "gas",
        "value": 3950,
        "category": "gas",
        "updated_at": "2021-03-08T11:30:57.924Z"
      },
      {
        "slug": "rent",
        "value": 85000,
        "category": "misc",
        "updated_at": "2021-03-08T11:30:57.924Z"
      },
      {
        "slug": "water",
        "value": 1000,
        "category": "misc",
        "updated_at": "2021-03-08T11:30:57.924Z"
      },
      {
        "slug": "administration",
        "value": 4000,
        "category": "misc",
        "updated_at": "2021-03-08T11:30:57.924Z"
      }
    ],
    "updated_at": "2021-03-08T11:30:57.924Z"
  },
  "last_updated_at": "2021-03-08T11:30:57.924Z",
  "total": 3,
  "data": [
    {
      "month": "202102",
      "updated_at": "2021-03-08T11:30:57.924Z",
      "total_value": 97640,
      "items": [
        {
          "slug": "electricity",
          "value": 3690,
          "category": "electricity",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "gas",
          "value": 3950,
          "category": "gas",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "rent",
          "value": 85000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "water",
          "value": 1000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "administration",
          "value": 4000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        }
      ]
    },
    {
      "month": "202101",
      "updated_at": "2021-03-08T11:30:57.924Z",
      "total_value": 97370,
      "items": [
        {
          "slug": "electricity",
          "value": 3750,
          "category": "electricity",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "gas",
          "value": 3620,
          "category": "gas",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "rent",
          "value": 85000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "water",
          "value": 1000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        },
        {
          "slug": "administration",
          "value": 4000,
          "category": "misc",
          "updated_at": "2021-03-08T11:30:57.924Z"
        }
      ]
    }
  ]
}
 */
@JsonClass(generateAdapter = true)
data class Bills(
    val total: Int,
    val latest: Bill?,
    val data: List<Bill>,
    @Json(name = "last_updated_at")
    val lastUpdatedAt: String?
)

@JsonClass(generateAdapter = true)
data class BillDetail(
    val data: Bill
)

@JsonClass(generateAdapter = true)
data class Bill(
    val month: String,
    @Json(name = "total_value")
    val totalValue: Int,
    val items: List<Item>,
    @Json(name = "updated_at")
    val updatedAt: String
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        val title: String,
        val slug: String,
        val value: Int,
        val category: Category,
    ) {
        enum class Slug {
            @Json(name = "rent")
            HouseRent,

            @Json(name = "water")
            Water,

            @Json(name = "gas")
            Gas,

            @Json(name = "electricity")
            Electricity,

            @Json(name= "membership_yearly")
            MembershipYearly,

            @Json(name = "administration")
            Administration,
        }

        enum class Category {
            @Json(name = "electricity")
            Electricity,

            @Json(name = "gas")
            Gas,

            @Json(name = "misc")
            Other;

        }
    }
}