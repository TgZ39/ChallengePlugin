package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.Challenge
import tgz39.challengeplugin.utils.sendMessage

object RandomItemCraftChallenge : Challenge, Listener {

    override var isActive: Boolean = true
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-item-craft-challenge.active", value)
            Main.instance.saveConfig()
        }

    var playerItemCount: MutableMap<String /* Playername */, Int> = mutableMapOf()

    var itemList: MutableList<Material> = Material.entries.filter { it.isItem }.shuffled().toMutableList()

    var playerSkipCount: MutableMap<String, Int> = mutableMapOf()

    var maxSkipCount = 3
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-item-craft-challenge.skip-count", value)
            Main.instance.saveConfig()
        }

    var maxItemCount = 0
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-item-craft-challenge.max-item-count", value)
            Main.instance.saveConfig()
        }

    init {
        isActive = Main.instance.config.getBoolean("challenges.random-item-craft-challenge.active", false)
        maxItemCount =
            Main.instance.config.getInt("challenges.random-item-craft-challenge.max-item-count", Int.MAX_VALUE)
        maxSkipCount = Main.instance.config.getInt("challenges.random-item-craft-challenge.skip-count", 3)
        run()
    }

    override fun guiItem(): ItemStack {
        val item = ItemStack(Material.FIREWORK_ROCKET, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.displayName(
            Component.text("Random Item Craft Challenge").decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD)
        )
        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))

        }

        lore.add(Component.text("").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
        lore.add(
            Component.text("Max Skips: $maxSkipCount").color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
        )
        if (maxItemCount == 0) {
            lore.add(
                Component.text("Max Items: No Limit").color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
            )
        } else {
            lore.add(
                Component.text("Max Items: $maxItemCount").color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.ITALIC, false)
            )
        }
        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.name !in playerItemCount) {
            playerItemCount[event.player.name] = 0
        }

        if (event.player.name !in playerSkipCount) {
            playerSkipCount[event.player.name] = 3
        }

        displayItem(event.player)
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {
                if (!isActive) return
                if (!Timer.isActive) return

                // check if player has item
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.name !in playerItemCount.keys) {
                        playerItemCount[player.name] = 0
                    }
                    if (player.inventory.contains(itemList[playerItemCount[player.name]!!])) {

                        sendMessage(
                            player,
                            "Item collected",
                            formatItemName(itemList[playerItemCount[player.name]!!].name),
                            NamedTextColor.WHITE
                        )
                        playerItemCount[player.name] = playerItemCount[player.name]!! + 1
                        sendMessage(
                            player,
                            "New Item",
                            formatItemName(itemList[playerItemCount[player.name]!!].name),
                            NamedTextColor.WHITE
                        )

                        displayItem(player)

                        if (playerItemCount[player.name]!! >= maxItemCount) {
                            Timer.isActive = false
                            sendMessage(player, "Random Item Challenge", "You won the Challenge!", NamedTextColor.WHITE)
                            for (_player in Bukkit.getOnlinePlayers()) {
                                if (_player.name == player.name) continue
                                sendMessage(
                                    _player,
                                    "Random Item Challenge",
                                    "${player.name} has won the Challenge!",
                                    NamedTextColor.WHITE
                                )
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.instance, 0, 1)

    }

    fun displayItem(player: Player) {
        player.showTitle(
            Title.title(
                Component.text(""), Component
                    .text("New Item: ")
                    .color(NamedTextColor.WHITE)
                    .decoration(TextDecoration.BOLD, false)
                    .append(
                        Component
                            .text(formatItemName(itemList[playerItemCount[player.name]!!].name))
                            .color(NamedTextColor.GOLD)
                            .decoration(TextDecoration.BOLD, true)
                    )
            )
        )
    }

    fun resetItemCounts() {
        for (player in Bukkit.getOnlinePlayers()) {
            playerItemCount[player.name] = 0
        }
    }

    fun resetItems() {
        itemList = Material.entries.filter { it.isItem }.shuffled().toMutableList()
    }

    fun formatItemName(itemName: String): String {
        val noUnderscores = itemName.replace("_", " ")
        val lowerCase = noUnderscores.lowercase()
        return lowerCase.split(" ").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    }

    fun displayWinner() {
        var message = Component
            .text("Random Item Challenge: \n")
            .color(NamedTextColor.GOLD)
            .decoration(TextDecoration.BOLD, true)
        var place = 1
        for (player in playerItemCount.entries.sortedByDescending { it.value }.associate { it.key to it.value }) {
            message = message
                .append(
                    Component.text("   ${place++}. ${player.key}: ${player.value - (3 - playerSkipCount[player.key]!!)}\n")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, false)
                )
        }

        Bukkit.broadcast(message)
    }

    fun resetSkips() {
        for (player in playerSkipCount) {
            playerSkipCount[player.key] = maxSkipCount
        }
    }

}