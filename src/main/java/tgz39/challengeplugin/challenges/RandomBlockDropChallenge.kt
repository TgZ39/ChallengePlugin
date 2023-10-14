package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.Challenge

object RandomBlockDropChallenge : Listener, Challenge {

    override var isActive = false
    var blockDrops: MutableMap<Material, Material> = mutableMapOf()

    init {
        updateConfig()
        generateRandomDrops()
    }

    override fun updateConfig() {
        val config = Main.instance.config
        isActive = config.getBoolean("challenges.random-block-drop-challenge.active")
    }

    override fun guiItem(): ItemStack {
        val item = ItemStack(Material.END_PORTAL_FRAME, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.displayName(
            Component.text("Random Block Drop Challenge").decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD)
        )

        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))

        }

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    fun generateRandomDrops() {
        var items = Material.entries.filter { it.isItem }.toMutableList()
        blockDrops.clear()

        for (material in Material.entries) {
            while (true) {
                if (items.isNotEmpty()) {
                    val randomItem = items.random()
                    blockDrops[material] = randomItem
                    items.remove(randomItem)
                    break
                } else {
                    items = Material.entries.filter { it.isItem }.toMutableList()
                }
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {

        if (!isActive) return
        if (!Timer.isActive) return

        val block = event.block

        block.world.dropItemNaturally(block.location, ItemStack(blockDrops.getValue(block.type)))
        block.type = Material.AIR
    }

}