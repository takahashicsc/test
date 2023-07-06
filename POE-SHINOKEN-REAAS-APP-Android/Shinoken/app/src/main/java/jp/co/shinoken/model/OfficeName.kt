package jp.co.shinoken.model

enum class OfficeName(prefecture: String, office: String) {
    HOKKAIDO("北海道","札幌オフィス"),
    AOMORI("青森県","仙台オフィス"),
    IWATE("岩手県","仙台オフィス"),
    MIYAGI("宮城県","仙台オフィス"),
    AKITA("秋田県","仙台オフィス"),
    YAMAGATA("山形県","仙台オフィス"),
    FUKUSHIMA("福島県","仙台オフィス"),
    IBARAGI("茨城県","東京オフィス"),
    TOCHIGI("栃木県","東京オフィス"),
    GUNMA("群馬県","東京オフィス"),
    SAITAMA("埼玉県","東京オフィス"),
    CHIBA("千葉県","東京オフィス"),
    TOKYO("東京都","東京オフィス"),
    KANAGAWA("神奈川県","東京オフィス"),
    NIIGATA("新潟県","名古屋オフィス"),
    TOYAMA("富山県","名古屋オフィス"),
    ISHIKAWA("石川県","名古屋オフィス"),
    FUKUI("福井県","名古屋オフィス"),
    YAMANASHI("山梨県","名古屋オフィス"),
    NAGANO("長野県","名古屋オフィス"),
    GIF("岐阜県","名古屋オフィス"),
    SHIZUOKA("静岡県","名古屋オフィス"),
    AICHI("愛知県","名古屋オフィス"),
    MIE("三重県","大阪オフィス"),
    SHIGA("滋賀県","大阪オフィス"),
    KYOTO("京都府","大阪オフィス"),
    OSAKA("大阪府","大阪オフィス"),
    HYOGO("兵庫県","大阪オフィス"),
    NARA("奈良県","大阪オフィス"),
    WAKAYAMA("和歌山県","大阪オフィス"),
    TOTTORI("鳥取県","大阪オフィス"),
    SHIMANE("島根県","大阪オフィス"),
    OKAYAMA("岡山県","大阪オフィス"),
    HIROSHIMA("広島県","大阪オフィス"),
    YAMAGUCHI("山口県","大阪オフィス"),
    TOKUSHIMA("徳島県","福岡オフィス"),
    KAGAWA("香川県","福岡オフィス"),
    EHIME("愛媛県","福岡オフィス"),
    KOCHI("高知県","福岡オフィス"),
    FUKUOKA("福岡県","福岡オフィス"),
    SAGA("佐賀県","福岡オフィス"),
    NAGASAKI("長崎県","福岡オフィス"),
    KUMAMOTO("熊本県","福岡オフィス"),
    OITA("大分県","福岡オフィス"),
    MIYAZAKI("宮崎県","福岡オフィス"),
    KAGOSHIMA("鹿児島県","福岡オフィス"),
    OKINAWA("沖縄県","福岡オフィス");

    val prefecture = prefecture
    val office = office

    companion object {
        fun getOfficeName(prefecture: String): String? {
            return try {
                val value = values().first { it.prefecture == prefecture }
                value.office
            } catch (e : NoSuchElementException) {
                null
            }
        }
    }

}