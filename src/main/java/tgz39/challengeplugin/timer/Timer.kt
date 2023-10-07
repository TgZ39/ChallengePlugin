package tgz39.challengeplugin.timer

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main

object Timer {

    //val timer = Main.timer

    var isActive = false
    var time = 0
    var ticks = 0

    init {
        run()
        initTime()
    }

    private fun initTime() {
        time = if (Main.instance.config.getBoolean("timer.save-time-between-sessions")) Main.instance.config.getInt("timer.time")
        else 0
    }

    fun getFormated(): String {

        val seconds = time % 60
        val minutes = (time / 60) % 60
        val hours = time / 3600

        return "$hours:$minutes:$seconds"
    }

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

                if (isActive) {
                    ticks++
                    if (ticks >= 20) {
                        time++
                        ticks = 0
                        if (Main.instance.config.getBoolean("timer.save-time-between-sessions")) {
                            Main.instance.config.set("timer.time", time)
                            Main.instance.saveConfig()
                        }
                    }
                } else {
                    for (player in Bukkit.getServer().onlinePlayers) {
                        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 21, 255, true))
                    }
                }

            }
        }.runTaskTimer(Main.instance, 0, 1)
    }
}


