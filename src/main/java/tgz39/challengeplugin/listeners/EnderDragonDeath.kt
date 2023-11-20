package tgz39.challengeplugin.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer

class EnderDragonDeath : Listener {

    @EventHandler
    fun onDragonDeath(event: EntityDeathEvent) {
        if (event.entityType == EntityType.ENDER_DRAGON && Timer.isActive && Main.instance.config.getBoolean("timer.stop-timer-on-dragon-death")) {
            Timer.isActive = false
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                    Component.text("Challenge: ").decoration(TextDecoration.BOLD, true).color(NamedTextColor.GOLD)
                        .append(
                            Component.text("Challenge completed!")
                                .decoration(TextDecoration.BOLD, false)
                                .color(NamedTextColor.WHITE)
                        )
                )
            }
        }
    }
}