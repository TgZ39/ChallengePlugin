package tgz39.challengeplugin.utils

fun isNumber(text: String): Boolean { // check if string is an Integer
    try {
        text.toInt()
    } catch (e: Exception) {
        return false
    }

    return true
}
