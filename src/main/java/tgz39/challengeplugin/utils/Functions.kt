package tgz39.challengeplugin.utils

fun isNumber(text: String): Boolean {
    try {
        text.toInt()
    } catch (e: Exception) {
        return false
    }

    return true
}
