package tgz39.challengeplugin.listeners

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import tgz39.challengeplugin.Main

class DeathLisenter : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {

        val timer = Main.timer
        val deadPlayer = event.player
        val config = Main.instance.config

        if (timer.isActive && config.getBoolean("timer.stop-timer-on-death")) {
            timer.isActive = false
            Bukkit.broadcast(Component.text("Challenge failed. " + deadPlayer.name + " died."))

            for (player in Bukkit.getServer().onlinePlayers) {
                if (player.gameMode == GameMode.SURVIVAL) {
                    player.gameMode = GameMode.SPECTATOR
                }
            }
            event.isCancelled = true
        }

    }

}