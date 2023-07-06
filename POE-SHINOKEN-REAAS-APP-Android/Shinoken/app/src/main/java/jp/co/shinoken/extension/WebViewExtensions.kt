package jp.co.shinoken.extension

fun String.convertHtmlString(): String {
    return this.replace("#", "")
}