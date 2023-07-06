package jp.co.shinoken.model

import jp.co.shinoken.R

enum class HomeMenu(
    val titleRes: Int,
    val subTitleRes: Int? = null,
    val iconRes: Int
) {
    Announcement(
        titleRes = R.string.home_item_announcement_text,
        iconRes = R.drawable.ic_menu_icon1
    ),
    Charge(titleRes = R.string.charges_title, iconRes = R.drawable.ic_menu_icon2),
    Account(titleRes = R.string.home_item_account_text, iconRes = R.drawable.ic_menu_icon3),
    Contact(titleRes = R.string.contact_title, iconRes = R.drawable.ic_menu_icon4),
    Faq(titleRes = R.string.faq_title, iconRes = R.drawable.ic_menu_icon5),
    Manual(titleRes = R.string.manual_title, iconRes = R.drawable.ic_menu_icon6),
    Coupons(titleRes = R.string.coupon_title, iconRes = R.drawable.ic_menu_icon7),
    Garbage(titleRes = R.string.garbages_title, iconRes = R.drawable.ic_menu_icon8),
    Topic(titleRes = R.string.topics_title, iconRes = R.drawable.ic_logo),
}