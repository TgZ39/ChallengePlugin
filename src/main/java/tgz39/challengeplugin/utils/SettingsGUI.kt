package tgz39.challengeplugin.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.challenges.LavaChallenge
import tgz39.challengeplugin.challenges.RandomEffectChallenge
import tgz39.challengeplugin.challenges.RandomMobChallenge

object SettingsGUI : Listener {

    private var inventory = Bukkit.createInventory(null, 9, Component.text("Settings"))

    init {
        loadconfig()
    }

    private fun updateInventory() {
        inventory.setItem(0, LavaChallenge.guiItem())
        inventory.setItem(1, RandomMobChallenge.guiItem())
        inventory.setItem(2, RandomEffectChallenge.guiItem())
    }

    fun openInvetory(player: Player) {
        updateInventory()
        player.openInventory(inventory)
    }

    fun openInvetory(player: HumanEntity) {
        updateInventory()
        player.openInventory(inventory)
    }

    private fun loadconfig() {
        val plugin = Main.instance

        LavaChallenge.isActive = plugin.config.getBoolean("challenges.lava-challenge.active")
        RandomMobChallenge.isActive = plugin.config.getBoolean("challenges.random-mob-challenge.active")
        RandomEffectChallenge.isActive = plugin.config.getBoolean("challenges.random-effect-challenge.active")
    }

    @EventHandler
    fun onItemClick(event: InventoryClickEvent) {

        if (event.view.title() != Component.text("Settings")) return

        val player = event.whoClicked
        val item = event.currentItem
        val config = Main.instance.config

        if (item?.displayName() == LavaChallenge.guiItem().displayName()) {
            if (!LavaChallenge.isActive) {
                LavaChallenge.isActive = true
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Lava Challenge has been enabled.")
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.lava-challenge.active", true)
            } else {
                LavaChallenge.isActive = false
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Lava Challenge has been disabled.")
                                .color(NamedTextColor.RED)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.lava-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == RandomMobChallenge.guiItem().displayName()) {
            if (!RandomMobChallenge.isActive) {
                RandomMobChallenge.isActive = true
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Random Mob Challenge has been enabled.")
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.random-mob-challenge.active", true)
            } else {
                RandomMobChallenge.isActive = false
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Random Mob Challenge has been disabled.")
                                .color(NamedTextColor.RED)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.random-mob-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == RandomEffectChallenge.guiItem().displayName()) {
            if (!RandomEffectChallenge.isActive) {
                RandomEffectChallenge.isActive = true
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Random Effect Challenge has been enabled.")
                                .color(NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.random-effect-challenge.active", true)
            } else {
                RandomEffectChallenge.isActive = false
                Bukkit.broadcast(
                    Component
                        .text("Challenges: ")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                            Component
                                .text("Random Effect Challenge has been disabled.")
                                .color(NamedTextColor.RED)
                                .decoration(TextDecoration.BOLD, false)
                        )
                )
                config.set("challenges.random-effect-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        event.isCancelled = true
        openInvetory(player)
    }

    @EventHandler
    fun onItemDrag(event: InventoryDragEvent) {

        if (event.view.title() != Component.text("Settings")) return

        event.isCancelled = true
    }
}