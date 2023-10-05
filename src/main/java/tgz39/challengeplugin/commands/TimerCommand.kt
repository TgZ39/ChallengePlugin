package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import tgz39.challengeplugin.Main
import java.lang.NumberFormatException

class TimerCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val timer = Main.timer

        if (!sender.hasPermission("challengeplugin.commands.timer")) {
            sender.sendMessage("You are not allowed to run this command.")
            return false
        }

        if (args?.size == 0) {
            sender.sendMessage(Component.text("Usage: /timer [resume, pause, reset, set]"))
        }

        when (args?.get(0)?.lowercase()) {
            "resume" -> {
                timer.isActive = true
                sender.sendMessage(Component.text("Timer has been enabled."))
            }

            "pause" -> {
                timer.isActive = false
                sender.sendMessage(Component.text("Timer has been paused."))
            }

            "reset" -> {
                timer.isActive = false
                timer.time = 0
                timer.ticks = 0
                sender.sendMessage(Component.text("Timer has been reset."))
            }

            "set" -> {
                if (args.size < 2) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>"))
                    return false
                }
                try {
                    timer.time = args[1].toInt()

                } catch (e: NumberFormatException) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>"))
                    return false
                }
            }
        }

        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {

        val list: MutableList<String> = mutableListOf()

        if (args?.size == 1) {
            list.add("resume")
            list.add("pause")
            list.add("reset")
            list.add("set")
        }

        return list
    }
}