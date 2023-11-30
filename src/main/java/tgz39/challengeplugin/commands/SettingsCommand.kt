package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import tgz39.challengeplugin.challenges.*
import tgz39.challengeplugin.utils.SettingsGUI
import tgz39.challengeplugin.utils.isNumber

class SettingsCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        fun sendUsageError(text: String) {
            sender.sendMessage(Component.text(text).color(NamedTextColor.RED))
        }

        fun sendMessage(text: String, color: NamedTextColor) {
            Bukkit.broadcast(
                Component
                    .text("Challenges: ")
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.BOLD, true)
                    .append(
                        Component
                            .text(text)
                            .color(color)
                            .decoration(TextDecoration.BOLD, false)
                    )
            )
        }

        if (!sender.hasPermission("challenge-plugin.commands.settings")) {
            sendUsageError("You are not allowed to use this command.")
            return false
        }

        when (args?.size) {

            0 -> {

                if (sender !is Player) {

                    sendUsageError("You need to be a player to run this command.")
                    return false
                }

                val player: Player = sender
                SettingsGUI.openInvetory(player)

                return false
            }

            1, 2 -> {
                when (args[0].lowercase()) {
                    "random-block-drop-challenge" -> {
                        if (args[1].lowercase() == "reset-drops") {

                            RandomBlockDropChallenge.generateRandomDrops()
                            sendMessage("Block drops have been randomized.", NamedTextColor.WHITE)
                        } else {

                            sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                        }
                    }

                    "random-item-collect-challenge" -> {
                        when (args[1].lowercase()) {
                            "reset-skips" -> {

                                RandomItemCollectChallenge.resetSkips()
                                sendMessage("Player Skips have been reset.", NamedTextColor.WHITE)
                            }

                            "reset-item-counts" -> {

                                RandomItemCollectChallenge.resetItemCounts()
                                sendMessage("Item counts have been reset.", NamedTextColor.WHITE)
                            }

                            "reset-items" -> {

                                RandomItemCollectChallenge.resetItems()
                                sendMessage("Items have been randomized.", NamedTextColor.WHITE)
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    else -> {

                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                }
            }

            in 3..Int.MAX_VALUE -> {
                when (args!![0].lowercase()) {
                    "random-effect-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        RandomEffectChallenge.isActive = true
                                        sendMessage("Random Effect Challenge has been enabled.", NamedTextColor.GREEN)
                                    }

                                    "false" -> {

                                        RandomEffectChallenge.isActive = false
                                        sendMessage("Random Effect Challenge has been disabled.", NamedTextColor.RED)
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "min-delay" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() >= RandomEffectChallenge.maxDelay) {

                                        sendUsageError("Min Delay cannot be larger that Max Delay.")
                                        return false
                                    }

                                    RandomEffectChallenge.minDelay = args[2].toInt()
                                    sendMessage(
                                        "Min delay changed to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                    RandomEffectChallenge.delay = RandomEffectChallenge.nextDelay()
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            "max-delay" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() <= RandomEffectChallenge.minDelay) {

                                        sendUsageError("Max Delay cannot be smaller that Min Delay.")
                                        return false
                                    }

                                    RandomEffectChallenge.maxDelay = args[2].toInt()
                                    sendMessage(
                                        "Max delay changed to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                    RandomEffectChallenge.delay = RandomEffectChallenge.nextDelay()
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            "min-level" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() >= RandomEffectChallenge.maxLevel) {

                                        sendUsageError("Min Level cannot be larger that Max Level.")
                                        return false
                                    }

                                    RandomEffectChallenge.minLevel = args[2].toInt()
                                    sendMessage(
                                        "Min level changed to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            "max-level" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() <= RandomEffectChallenge.minLevel) {

                                        sendUsageError("Max Level cannot be smaller that Min Level.")
                                        return false
                                    }

                                    RandomEffectChallenge.maxLevel = args[2].toInt()
                                    sendMessage(
                                        "Max level changed to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            "infinite-effect-duration" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        RandomEffectChallenge.infiniteEffectDuration = true
                                        sendMessage(
                                            "Infinite effect duration has been enabled.",
                                            NamedTextColor.GREEN
                                        )
                                    }

                                    "false" -> {

                                        RandomEffectChallenge.infiniteEffectDuration = false
                                        sendMessage(
                                            "Infinite effect duration has been disabled.",
                                            NamedTextColor.RED
                                        )
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "effect-duration" -> {
                                if (isNumber(args[2])) {

                                    RandomEffectChallenge.effectDuration = args[2].toInt()
                                    sendMessage(
                                        "Random Effect Challenge effect duration changed to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    "random-mob-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        RandomMobChallenge.isActive = true
                                        sendMessage("Random Mob Challenge has been enabled.", NamedTextColor.GREEN)
                                    }

                                    "false" -> {

                                        RandomMobChallenge.isActive = false
                                        sendMessage("Random Mob Challenge has been disabled.", NamedTextColor.RED)
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "min-delay" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() >= RandomMobChallenge.maxDelay) {

                                        sendUsageError("Min Delay cannot be larger that Max Delay.")
                                        return false
                                    }

                                    RandomMobChallenge.minDelay = args[2].toInt()
                                    sendMessage("Min delay changed to " + args[2] + ".", NamedTextColor.WHITE)
                                    RandomMobChallenge.delay = RandomMobChallenge.nextDelay()
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }

                            }

                            "max-delay" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() <= RandomMobChallenge.minDelay) {

                                        sendUsageError("Max Delay cannot be smaller that Min Delay.")
                                        return false
                                    }

                                    RandomMobChallenge.maxDelay = args[2].toInt()
                                    sendMessage("Max delay changed to " + args[2] + ".", NamedTextColor.WHITE)
                                    RandomMobChallenge.delay = RandomMobChallenge.nextDelay()
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }

                        }
                    }

                    "lava-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        LavaChallenge.isActive = true
                                        sendMessage("Lava Challenge has been enabled.", NamedTextColor.GREEN)
                                    }

                                    "false" -> {

                                        LavaChallenge.isActive = false
                                        sendMessage("Lava Challenge has been disabled.", NamedTextColor.RED)
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "lava-spawn-height" -> {
                                if (isNumber(args[2])) {

                                    LavaChallenge.lavaSpawnHeight = args[2].toInt()
                                    sendMessage(
                                        "Lava spawn height has been set to " + args[2] + ".",
                                        NamedTextColor.WHITE
                                    )
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    "health-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        HealthChallenge.isActive = true
                                        sendMessage("Health Challenge has been enabled.", NamedTextColor.GREEN)
                                    }

                                    "false" -> {

                                        HealthChallenge.isActive = false
                                        sendMessage("Health Challenge has been disabled.", NamedTextColor.RED)
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "health" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() < 1) {
                                        sendUsageError("You can only input numbers higher that are 1 or higher.")
                                        return false
                                    }

                                    HealthChallenge.health = args[2].toDouble()
                                    sendMessage("Health has been set to " + args[2] + ".", NamedTextColor.WHITE)
                                    HealthChallenge.updateHealth()
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    "random-block-drop-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        RandomBlockDropChallenge.isActive = true
                                        sendMessage(
                                            "Random Block Drop Challenge has been enabled.",
                                            NamedTextColor.GREEN
                                        )
                                    }

                                    "false" -> {

                                        RandomBlockDropChallenge.isActive = false
                                        sendMessage(
                                            "Random Block Drop Challenge has been disabled.",
                                            NamedTextColor.RED
                                        )
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    "random-item-collect-challenge" -> {
                        when (args[1].lowercase()) {
                            "active" -> {
                                when (args[2].lowercase()) {
                                    "true" -> {

                                        RandomItemCollectChallenge.isActive = true
                                        sendMessage(
                                            "Random Item Collect Challenge has been enabled.",
                                            NamedTextColor.GREEN
                                        )
                                    }

                                    "false" -> {

                                        RandomItemCollectChallenge.isActive = false
                                        sendMessage(
                                            "Random Item Collect Challenge has been disabled.",
                                            NamedTextColor.RED
                                        )
                                    }

                                    else -> {

                                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                    }
                                }
                            }

                            "set-max-skips" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() < 0) {
                                        sendUsageError("You can only input numbers higher that are 0 or higher.")
                                        return false
                                    }

                                    RandomItemCollectChallenge.maxSkipCount = args[2].toInt()
                                    sendMessage("Max Skip count has been set to ${args[2]}.", NamedTextColor.WHITE)
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            "set-max-item-count" -> {
                                if (isNumber(args[2])) {
                                    if (args[2].toInt() < 0) {
                                        sendUsageError("You can only input numbers higher that are 0 (No Limit) or higher.")
                                        return false
                                    }

                                    RandomItemCollectChallenge.maxItemCount = args[2].toInt()
                                    sendMessage("Max Item count has been set to ${args[2]}.", NamedTextColor.WHITE)
                                } else {

                                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                                }
                            }

                            else -> {

                                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                            }
                        }
                    }

                    else -> {

                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                }
            }

            else -> {

                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
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

        fun listItem(text: String, index: Int) {
            if (text.lowercase().contains(args!![index])) list.add(text)
        }

        if (args?.size == 1) {

            listItem("random-effect-challenge", 0)
            listItem("random-mob-challenge", 0)
            listItem("lava-challenge", 0)
            listItem("health-challenge", 0)
            listItem("random-block-drop-challenge", 0)
            listItem("random-item-collect-challenge", 0)

        } else if (args?.size == 2) {
            when (args[0].lowercase()) {
                "random-effect-challenge" -> {

                    listItem("active", 1)
                    listItem("min-delay", 1)
                    listItem("max-delay", 1)
                    listItem("min-level", 1)
                    listItem("max-level", 1)
                    listItem("infinite-effect-duration", 1)
                    listItem("effect-duration", 1)

                }

                "random-mob-challenge" -> {

                    listItem("active", 1)
                    listItem("min-delay", 1)
                    listItem("max-delay", 1)

                }

                "lava-challenge" -> {

                    listItem("active", 1)
                    listItem("lava-spawn-height", 1)

                }

                "health-challenge" -> {

                    listItem("active", 1)
                    listItem("health", 1)

                }

                "random-block-drop-challenge" -> {

                    listItem("active", 1)
                    listItem("reset-drops", 1)

                }

                "random-item-collect-challenge" -> {

                    listItem("active", 1)
                    listItem("reset-items", 1)
                    listItem("reset-item-counts", 1)
                    listItem("reset-skips", 1)
                    listItem("set-max-skips", 1)
                    listItem("set-max-item-count", 1)

                }

            }

        } else if (args!!.size == 3 && args[1].lowercase() == "active") {

            listItem("true", 2)
            listItem("false", 2)

        }

        return list
    }
}