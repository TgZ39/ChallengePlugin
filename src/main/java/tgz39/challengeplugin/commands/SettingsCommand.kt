package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tgz39.challengeplugin.utils.SettingsGUI

class SettingsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage(Component.text("You need to be a player to run this command.").color(NamedTextColor.RED))
            return false
        }
        val player: Player = sender

        if (!player.hasPermission("challengeplugin.commands.settings")) {
            player.sendMessage(Component.text("You are not allowed to use this command.").color(NamedTextColor.RED))
            return false
        }

        SettingsGUI.openInvetory(player)

        return false
    }
}