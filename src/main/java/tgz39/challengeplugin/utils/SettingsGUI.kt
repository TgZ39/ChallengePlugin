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
import tgz39.challengeplugin.challenges.lavaChallenge

class SettingsGUI : Listener{

    private var inventory = Bukkit.createInventory(null, 9, Component.text("Settings"))

    init {
        updateInventory()
    }

    private fun updateInventory() {
        inventory.setItem(0, lavaChallenge.guiItem())
    }

    fun openInvetory(player: Player) {
        updateInventory()
        player.openInventory(inventory)
    }
    fun openInvetory(player: HumanEntity) {
        updateInventory()
        player.openInventory(inventory)
    }

    @EventHandler
    fun onItemClick(event: InventoryClickEvent) {

        if (event.view.title() != Component.text("Settings")) return

        val player = event.whoClicked
        val item = event.currentItem

        if (item?.displayName() == lavaChallenge.guiItem().displayName()) {
            if (!lavaChallenge.isActive) {
                lavaChallenge.isActive = true
                Bukkit.broadcast(Component
                    .text("ChallengesPlugin: ")
                    .color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component
                        .text("LavaChallenge has been enabled.")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, false)))
            } else {
                lavaChallenge.isActive = false
                Bukkit.broadcast(Component
                    .text("ChallengesPlugin: ")
                    .color(NamedTextColor.RED)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component
                        .text("LavaChallenge has been disabled.")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, false)))
            }
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