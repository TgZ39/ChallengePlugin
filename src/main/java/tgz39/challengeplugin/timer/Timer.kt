package tgz39.challengeplugin.timer

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.challenges.RandomItemCollectChallenge

object Timer {

    var isActive = false
    var time = 0
        set(value) {
            field = value
            Main.instance.config.set("timer.time", value)
            Main.instance.saveConfig()
        }
    var ticks = 0
    var saveTimeBetweenSessions = true
        set(value) {
            field = value
            Main.instance.config.set("timer.save-time-between-sessions", value)
            Main.instance.saveConfig()
        }
    var mode: Boolean = false // false = countup, true = countdown
        set(value) {
            field = value
            Main.instance.config.set("timer.mode", value)
            Main.instance.saveConfig()
        }

    init {
        val config = Main.instance.config

        saveTimeBetweenSessions = config.getBoolean("timer.save-time-between-sessions")
        time = if (saveTimeBetweenSessions) config.getInt("timer.time") else 0

        run()
    }

    fun getFormated(): String {

        val seconds =
            if (time % 60 >= 10) {
                (time % 60).toString()
            } else {
                "0" + (time % 60).toString()
            }

        val minutes =
            if ((time / 60) % 60 >= 10) {
                ((time / 60) % 60).toString()
            } else {
                "0" + ((time / 60) % 60).toString()
            }
        val hours = time / 3600

        return "$hours:$minutes:$seconds"
    }

    // display timer in actionbar
    fun sendActionBar() {
        for (player in Bukkit.getServer().onlinePlayers) {
            if (isActive) {
                player.sendActionBar(
                    Component
                        .text(getFormated())
                        .decoration(TextDecoration.BOLD, true)
                        .color(NamedTextColor.GOLD)
                )
            } else {
                player.sendActionBar(
                    Component
                        .text(getFormated())
                        .decoration(TextDecoration.BOLD, true)
                        .color(NamedTextColor.GOLD)
                        .append(
                            Component
                                .text(" (paused)")
                                .decoration(TextDecoration.BOLD, false)
                                .decoration(TextDecoration.ITALIC, true)
                        )
                )
            }
        }
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {

                sendActionBar()

                // make timer go up
                if (isActive) {
                    ticks++
                    if (ticks >= 20) {
                        if (!mode) {
                            time++
                        } else {
                            time--
                        }
                        ticks = 0
                    }
                    if (time <= 0 && mode) {
                        isActive = false
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(
                                Component.text("Timer: ").decoration(TextDecoration.BOLD, true)
                                    .color(NamedTextColor.GOLD)
                                    .append(
                                        Component.text("Time over!").decoration(TextDecoration.BOLD, false)
                                            .color(NamedTextColor.WHITE)
                                    )
                            )

                            RandomItemCollectChallenge.displayWinner()

                            if (player.gameMode == GameMode.SURVIVAL) {
                                player.gameMode = GameMode.SPECTATOR
                            }
                        }
                    }
                } else { // give player effects if timer is not active
                    for (player in Bukkit.getServer().onlinePlayers) {
                        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 21, 255, true))
                        player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 21, 255, true))
                    }
                }

            }
        }.runTaskTimer(Main.instance, 0, 1)
    }
}


