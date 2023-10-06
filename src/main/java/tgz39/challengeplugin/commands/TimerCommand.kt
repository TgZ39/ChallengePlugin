package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import tgz39.challengeplugin.challenges.RandomEffectChallenge
import tgz39.challengeplugin.challenges.RandomMobChallenge
import tgz39.challengeplugin.timer.Timer

class TimerCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {


        if (!sender.hasPermission("challengeplugin.commands.timer")) {
            sender.sendMessage("You are not allowed to run this command.")
            return false
        }

        if (args?.size == 0) {
            sender.sendMessage(Component.text("Usage: /timer [resume, pause, reset, set]"))
        }

        when (args?.get(0)?.lowercase()) {
            "resume" -> {
                Timer.isActive = true
                sender.sendMessage(Component.text("Timer has been enabled."))
            }

            "pause" -> {
                Timer.isActive = false
                sender.sendMessage(Component.text("Timer has been paused."))
            }

            "reset" -> {
                Timer.isActive = false
                Timer.time = 0
                Timer.ticks = 0
                RandomMobChallenge.time = 0
                RandomEffectChallenge.time = 0
                sender.sendMessage(Component.text("Timer has been reset."))
            }

            "set" -> {
                if (args.size < 2) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>"))
                    return false
                }
                try {
                    Timer.time = args[1].toInt()

                } catch (e: NumberFormatException) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>"))
                    return false
                }
            }
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {

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