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
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.challenges.*
import tgz39.challengeplugin.utils.SettingsGUI
import tgz39.challengeplugin.utils.isNumber

class SettingsCommand : CommandExecutor, TabCompleter {

    private fun sendMessage(text: String, color: NamedTextColor) {
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


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        fun sendUsageError(text: String) {
            sender.sendMessage(Component.text(text).color(NamedTextColor.RED))
        }

        val config = Main.instance.config

        if (!sender.hasPermission("challengeplugin.commands.settings")) {
            sender.sendMessage(Component.text("You are not allowed to use this command.").color(NamedTextColor.RED))
            return false
        }

        if (args?.size == 0) {

            if (sender !is Player) {
                sender.sendMessage(
                    Component.text("You need to be a player to run this command.").color(NamedTextColor.RED)
                )
                return false
            }

            val player: Player = sender
            SettingsGUI.openInvetory(player)

            return false

        } else if (args?.size == 1 || args?.size == 2) {

            if (args[1].lowercase() == "reset") {
                RandomBlockDropChallenge.generateRandomDrops()
                sendMessage("Random Block Drop Challenge drops have been randomized.", NamedTextColor.WHITE)

            } else {
                sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
            }
        } else if (args?.size!! >= 3) {
            if (args[0].lowercase() == "random-effect-challenge") {
                if (args[1].lowercase() == "active") {
                    if (args[2].lowercase() == "true") {

                        RandomEffectChallenge.isActive = true
                        sendMessage("Random Effect Challenge has been enabled.", NamedTextColor.GREEN)

                    } else if (args[2].lowercase() == "false") {

                        RandomEffectChallenge.isActive = false
                        sendMessage("Random Effect Challenge has been disabled.", NamedTextColor.RED)
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }

                } else if (args[1].lowercase() == "min-delay") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() >= RandomEffectChallenge.maxDelay) {
                            sendUsageError("Min Delay cannot be larger that Max Delay.")
                            return false
                        }
                        RandomEffectChallenge.minDelay = args[2].toInt()
                        sendMessage(
                            "Random Effect Challenge min delay changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                        RandomEffectChallenge.delay = RandomEffectChallenge.nextDelay()
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "max-delay") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() <= RandomEffectChallenge.minDelay) {
                            sendUsageError("Max Delay cannot be smaller that Min Delay.")
                            return false
                        }
                        RandomEffectChallenge.maxDelay = args[2].toInt()
                        sendMessage(
                            "Random Effect Challenge max delay changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                        RandomEffectChallenge.delay = RandomEffectChallenge.nextDelay()
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "min-level") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() >= RandomEffectChallenge.maxLevel) {
                            sendUsageError("Min Level cannot be larger that Max Level.")
                            return false
                        }
                        RandomEffectChallenge.minLevel = args[2].toInt()
                        sendMessage(
                            "Random Effect Challenge min level changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "max-level") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() <= RandomEffectChallenge.minLevel) {
                            sendUsageError("Max Level cannot be larger that Min Level.")
                            return false
                        }
                        RandomEffectChallenge.maxLevel = args[2].toInt()
                        sendMessage(
                            "Random Effect Challenge max level changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "infinite-effect-duration") {
                    if (args[2].lowercase() == "true") {

                        RandomEffectChallenge.infiniteEffectDuration = true
                        sendMessage(
                            "Random Effect Challenge infinite effect duration has been enabled.",
                            NamedTextColor.GREEN
                        )

                    } else if (args[2].lowercase() == "false") {

                        RandomEffectChallenge.infiniteEffectDuration = false
                        sendMessage(
                            "Random Effect Challenge infinite effect duration has been disabled.",
                            NamedTextColor.RED
                        )
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "effect-duration") {
                    if (isNumber(args[2])) {
                        RandomEffectChallenge.effectDuration = args[2].toInt()
                        sendMessage(
                            "Random Effect Challenge effect duration changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else {
                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                }
            } else if (args[0].lowercase() == "random-mob-challenge") {
                if (args[1].lowercase() == "active") {
                    if (args[2].lowercase() == "true") {

                        RandomMobChallenge.isActive = true
                        sendMessage("Random Mob Challenge has been enabled.", NamedTextColor.GREEN)

                    } else if (args[2].lowercase() == "false") {

                        RandomMobChallenge.isActive = false
                        sendMessage("Random Mob Challenge has been disabled.", NamedTextColor.RED)
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "min-delay") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() >= RandomMobChallenge.maxDelay) {
                            sendUsageError("Min Delay cannot be larger that Max Delay.")
                            return false
                        }
                        RandomMobChallenge.minDelay = args[2].toInt()
                        sendMessage("Random Mob Challenge min delay changed to " + args[2] + ".", NamedTextColor.WHITE)
                        RandomMobChallenge.delay = RandomMobChallenge.nextDelay()
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "max-delay") {
                    if (isNumber(args[2])) {
                        if (args[2].toInt() <= RandomMobChallenge.minDelay) {
                            sendUsageError("Max Delay cannot be smaller that Min Delay.")
                            return false
                        }
                        RandomMobChallenge.maxDelay = args[2].toInt()
                        sendMessage("Random Mob Challenge max delay changed to " + args[2] + ".", NamedTextColor.WHITE)
                        RandomMobChallenge.delay = RandomMobChallenge.nextDelay()
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else {
                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                }
            } else if (args[0].lowercase() == "lava-challenge") {
                if (args[1].lowercase() == "active") {
                    if (args[2].lowercase() == "true") {

                        LavaChallenge.isActive = true
                        sendMessage("Lava Challenge has been enabled.", NamedTextColor.GREEN)

                    } else if (args[2].lowercase() == "false") {

                        LavaChallenge.isActive = false
                        sendMessage("Lava Challenge has been disabled.", NamedTextColor.RED)
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "lava-spawn-height") {
                    if (isNumber(args[2])) {
                        LavaChallenge.lavaSpawnHeight = args[2].toInt()
                        sendMessage(
                            "Lava Challenge lava spawn height changed to " + args[2] + ".",
                            NamedTextColor.WHITE
                        )
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                }
            } else if (args[0].lowercase() == "health-challenge") {
                if (args[1].lowercase() == "active") {
                    if (args[2].lowercase() == "true") {

                        HealthChallenge.isActive = true
                        sendMessage("Health Challenge has been enabled.", NamedTextColor.GREEN)
                        HealthChallenge.updateHealth()

                    } else if (args[2].lowercase() == "false") {

                        HealthChallenge.isActive = false
                        sendMessage("Health Challenge has been disabled.", NamedTextColor.RED)
                        HealthChallenge.updateHealth()

                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else if (args[1].lowercase() == "health") {
                    if (isNumber(args[2])) {

                        HealthChallenge.health = args[2].toDouble()
                        sendMessage("Health Challenge health changed to " + args[2], NamedTextColor.WHITE)
                        HealthChallenge.updateHealth()

                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else {
                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                }

            } else if (args[0].lowercase() == "random-block-drop-challenge") {
                if (args[1].lowercase() == "active") {
                    if (args[2].lowercase() == "true") {

                        RandomBlockDropChallenge.isActive = true
                        sendMessage("Random Block Drop Challenge has been enabled.", NamedTextColor.GREEN)

                    } else if (args[2].lowercase() == "false") {

                        RandomBlockDropChallenge.isActive = false
                        sendMessage("Random Block Drop Challenge has been disabled.", NamedTextColor.RED)
                    } else {
                        sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                    }
                } else {
                    sendUsageError("Usage: /settings <CHALLENGE> <OPTION> <VALUE>")
                }

            } else {
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

        if (args?.size == 1) {
            list.add("random-effect-challenge")
            list.add("random-mob-challenge")
            list.add("lava-challenge")
            list.add("health-challenge")
            list.add("random-block-drop-challenge")
        }

        if (args?.size == 2) {
            if (args[0].lowercase() == "random-effect-challenge") {

                list.add("active")
                list.add("min-delay")
                list.add("max-delay")
                list.add("min-level")
                list.add("max-level")
                list.add("infinite-effect-duration")
                list.add("effect-duration")

            } else if (args[0].lowercase() == "random-mob-challenge") {

                list.add("active")
                list.add("min-delay")
                list.add("max-delay")

            } else if (args[0].lowercase() == "lava-challenge") {

                list.add("active")
                list.add("lava-spawn-height")

            } else if (args[0].lowercase() == "health-challenge") {
                list.add("active")
                list.add("health")
            } else if (args[0].lowercase() == "random-block-drop-challenge") {
                list.add("active")
                list.add("reset")
            }
        }

        if (args?.size == 3) {
            if (args[0].lowercase() == "random-effect-challenge") {
                if (args[1].lowercase() == "active") {
                    list.add("true")
                    list.add("false")
                } else if (args[1].lowercase() == "infinite-effect-duration") {
                    list.add("true")
                    list.add("false")
                }
            } else if (args[0].lowercase() == "random-mob-challenge") {
                if (args[1].lowercase() == "active") {
                    list.add("true")
                    list.add("false")
                }
            } else if (args[0].lowercase() == "lava-challenge") {
                if (args[1].lowercase() == "active") {
                    list.add("true")
                    list.add("false")
                }
            } else if (args[0].lowercase() == "health-challenge") {
                if (args[1].lowercase() == "active") {
                    list.add("true")
                    list.add("false")
                }
            } else if (args[0].lowercase() == "random-block-drop-challenge") {
                if (args[1].lowercase() == "active") {
                    list.add("true")
                    list.add("false")
                }
            }
        }

        return list
    }
}