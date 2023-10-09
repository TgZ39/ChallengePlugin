package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.DefaultChallenge

object RandomBlockDropChallenge : Listener, DefaultChallenge {

    override var isActive = false
    var blockDrops: MutableMap<Material, Material> = mutableMapOf()
    var seed: Long = 0L

    init {
        generateRandomDrops()
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
        val usedBlocks: MutableList<Material> = mutableListOf()
        blockDrops.clear()

        for (material in Material.entries) {
            while (true) {
                val randomBlock = Material.entries.random()

                if (randomBlock !in usedBlocks) {
                    blockDrops[material] = randomBlock
                    usedBlocks.add(randomBlock)
                    break
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