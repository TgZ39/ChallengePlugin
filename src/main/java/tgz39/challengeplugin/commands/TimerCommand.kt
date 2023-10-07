package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.challenges.RandomEffectChallenge
import tgz39.challengeplugin.challenges.RandomMobChallenge
import tgz39.challengeplugin.timer.Timer

class TimerCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {


        if (!sender.hasPermission("challengeplugin.commands.timer")) {
            sender.sendMessage(Component.text("You are not allowed to use this command.").color(NamedTextColor.RED))
            return false
        }

        if (args?.size == 0) {
            sender.sendMessage(Component.text("Usage: /timer [resume, pause, reset, set]").color(NamedTextColor.RED))
        }

        when (args?.get(0)?.lowercase()) {
            "resume" -> {
                Timer.isActive = true
                sender.sendMessage(
                    Component.text("Timer: ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.GOLD).append(
                        Component.text("Enabled").decoration(TextDecoration.BOLD, false).color(NamedTextColor.WHITE)
                    )
                )
            }

            "pause" -> {
                Timer.isActive = false
                sender.sendMessage(
                    Component.text("Timer: ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.GOLD).append(
                        Component.text("Paused").decoration(TextDecoration.BOLD, false).color(NamedTextColor.WHITE)
                    )
                )
            }

            "reset" -> {
                Timer.isActive = false
                Timer.time = 0
                Timer.ticks = 0
                RandomMobChallenge.time = 0
                RandomEffectChallenge.time = 0
                sender.sendMessage(
                    Component.text("Timer: ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.GOLD).append(
                        Component.text("Reset").decoration(TextDecoration.BOLD, false).color(NamedTextColor.WHITE)
                    )
                )
                if (Main.instance.config.getBoolean("timer.save-time-between-sessions")) {
                    Main.instance.config.set("timer.time", Timer.time)
                    Main.instance.saveConfig()
                }
            }

            "set" -> {
                if (args.size < 2) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>").color(NamedTextColor.RED))
                    return false
                }
                try {
                    Timer.time = args[1].toInt()
                    sender.sendMessage(
                        Component.text("Timer: ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.GOLD)
                            .append(
                                Component.text("Set to: " + Timer.getFormated()).decoration(TextDecoration.BOLD, false)
                                    .color(NamedTextColor.WHITE)
                            )
                    )
                    if (Main.instance.config.getBoolean("timer.save-time-between-sessions")) {
                        Main.instance.config.set("timer.time", Timer.time)
                        Main.instance.saveConfig()
                    }

                } catch (e: NumberFormatException) {
                    sender.sendMessage(Component.text("Usage: /timer set <SECONDS>").color(NamedTextColor.RED))
                    return false
                }
            }

            else -> {
                sender.sendMessage(
                    Component.text("Usage: /timer [resume, pause, reset, set]").color(NamedTextColor.RED)
                )
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