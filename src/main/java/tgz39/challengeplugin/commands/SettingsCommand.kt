package tgz39.challengeplugin.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tgz39.challengeplugin.settingsGUI

class SettingsCommand : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Du musst ein Spieler sein um diesen Command benutzen zu können.")
            return false
        }
        val player: Player = sender

        if (!player.hasPermission("challengeplugin.commands.settings")) {
            player.sendMessage("Du darfst diesen Command nicht ausführen.")
            return false
        }

        settingsGUI.openInvetory(player)

        return false
    }
}