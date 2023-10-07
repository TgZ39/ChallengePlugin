package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.DefaultChallenge

object LavaChallenge : DefaultChallenge {

    init {
        run()
    }

    override var isActive: Boolean = false

    override fun guiItem(): ItemStack {

        val config = Main.instance.config
        val item = ItemStack(Material.LAVA_BUCKET, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.displayName(
            Component.text("Lava Challenge").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD)
        )

        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))

        }

        lore.add(Component.text(""))
        lore.add(Component.text("Height: " + config.get("challenges.lava-challenge.lava-spawn-height")).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE))

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {
                if (!isActive) return
                if (!Timer.isActive) return

                for (player in Bukkit.getServer().onlinePlayers) {

                    if (player.gameMode != GameMode.SURVIVAL) continue

                    val pos = player.location
                    pos.y += Main.instance.config.getInt("challenges.lava-challenge.lava-spawn-height")

                    pos.block.type = Material.LAVA
                }
            }
        }.runTaskTimer(Main.instance, 0, 1)

    }
}