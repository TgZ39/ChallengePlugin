package tgz39.challengeplugin.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

fun isNumber(text: String): Boolean { // check if string is an Integer
    try {
        text.toInt()
    } catch (e: Exception) {
        return false
    }

    return true
}

fun sendMessage(player: Player, messageSender: String, message: String, color: NamedTextColor) {
    player.sendMessage(
        Component
            .text("$messageSender: ")
            .color(NamedTextColor.GOLD)
            .decoration(TextDecoration.BOLD, true)
            .append(
                Component
                    .text(message)
                    .color(color)
                    .decoration(TextDecoration.BOLD, false)
            )
    )
}