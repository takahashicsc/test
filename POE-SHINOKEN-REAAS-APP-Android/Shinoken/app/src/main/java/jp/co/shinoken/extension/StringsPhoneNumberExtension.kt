package jp.co.shinoken.extension

val String?.toInternationalPhoneNumber: String?
    get() {
        val value = this ?: return this
        if (value.startsWith("0").not()) {
            return this
        }
        return value.replace(Regex("(^.*?)(0)"), "+81")
    }

val String?.toPhoneNumber: String?
    get() {
        val value = this ?: return this
        return value.replace("+81", "0")
    }