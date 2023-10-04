package tgz39.challengeplugin.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class TimerCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Du musst ein Spieler sein um diesen Command benutzen zu können.")
            return false
        }
        val player: Player = sender

        if (!player.hasPermission("challengeplugin.commands.timer")) {
            player.sendMessage("Du darfst diesen Command nicht ausführen.")
            return false
        }

        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {

        var list: MutableList<String> = mutableListOf()

        if (args?.size == 1) {
            list.add("resume")
            list.add("stop")
            list.add("reset")
        }

        return list
    }
}