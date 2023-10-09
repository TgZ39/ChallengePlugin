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
import tgz39.challengeplugin.challenges.*

object SettingsGUI : Listener {

    private var inventory = Bukkit.createInventory(null, 9, Component.text("Settings"))

    init {
        loadconfig()
    }

    private fun updateInventory() {
        inventory.setItem(0, LavaChallenge.guiItem())
        inventory.setItem(1, RandomMobChallenge.guiItem())
        inventory.setItem(2, RandomEffectChallenge.guiItem())
        inventory.setItem(3, HealthChallenge.guiItem())
        inventory.setItem(4, RandomBlockDropChallenge.guiItem())
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
        HealthChallenge.isActive = plugin.config.getBoolean("challenges.health-challenge.active")
        RandomBlockDropChallenge.isActive = plugin.config.getBoolean("challenges.random-block-drop-challenge.active")
    }

    @EventHandler
    fun onItemClick(event: InventoryClickEvent) {

        fun sendChallengeMessage(text: String, color: NamedTextColor) {
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

        if (event.view.title() != Component.text("Settings")) return

        val player = event.whoClicked
        val item = event.currentItem
        val config = Main.instance.config

        if (item?.displayName() == LavaChallenge.guiItem().displayName()) {
            if (!LavaChallenge.isActive) {
                LavaChallenge.isActive = true
                sendChallengeMessage("Lava Challenge has been enabled.", NamedTextColor.GREEN)
                config.set("challenges.lava-challenge.active", true)
            } else {
                LavaChallenge.isActive = false
                sendChallengeMessage("Lava Challenge has been disabled.", NamedTextColor.RED)
                config.set("challenges.lava-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == RandomMobChallenge.guiItem().displayName()) {
            if (!RandomMobChallenge.isActive) {
                RandomMobChallenge.isActive = true
                sendChallengeMessage("Random Mob Challenge has been enabled.", NamedTextColor.GREEN)
                config.set("challenges.random-mob-challenge.active", true)
            } else {
                RandomMobChallenge.isActive = false
                sendChallengeMessage("Random Mob Challenge has been disabled.", NamedTextColor.RED)
                config.set("challenges.random-mob-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == RandomEffectChallenge.guiItem().displayName()) {
            if (!RandomEffectChallenge.isActive) {
                RandomEffectChallenge.isActive = true
                sendChallengeMessage("Random Effect Challenge has been enabled.", NamedTextColor.GREEN)
                config.set("challenges.random-effect-challenge.active", true)
            } else {
                RandomEffectChallenge.isActive = false
                sendChallengeMessage("Random Effect Challenge has been disabled.", NamedTextColor.RED)
                config.set("challenges.random-effect-challenge.active", false)
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == HealthChallenge.guiItem().displayName()) {
            if (!HealthChallenge.isActive) {
                HealthChallenge.isActive = true
                sendChallengeMessage("Health Challenge has been enabled.", NamedTextColor.GREEN)
                config.set("challenges.health-challenge.active", true)
                HealthChallenge.updateHealth()
            } else {
                HealthChallenge.isActive = false
                sendChallengeMessage("Health Challenge has been disabled.", NamedTextColor.RED)
                config.set("challenges.health-challenge.active", false)
                HealthChallenge.updateHealth()
            }
            Main.instance.saveConfig()
        }

        if (item?.displayName() == RandomBlockDropChallenge.guiItem().displayName()) {
            if (!RandomBlockDropChallenge.isActive) {
                RandomBlockDropChallenge.isActive = true
                sendChallengeMessage("Random Block Drop Challenge has been enabled.", NamedTextColor.GREEN)
                config.set("challenges.random-block-drop-challenge.active", true)
            } else {
                RandomBlockDropChallenge.isActive = false
                sendChallengeMessage("Random Block Drop Challenge has been disabled.", NamedTextColor.RED)
                config.set("challenges.random-block-drop-challenge.active", false)
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