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

object LavaChallenge : DefaultChallenge{

    init {
        run()
    }

    override var isActive: Boolean = false

    override fun guiItem(): ItemStack {

        val item = ItemStack(Material.LAVA_BUCKET, 1)
        val itemMeta = item.itemMeta

        itemMeta.displayName(Component.text("LavaChallenge").decorate(TextDecoration.BOLD))

        if (isActive) {
            itemMeta.lore(mutableListOf(Component.text("Enabled").color(NamedTextColor.GREEN)))

        } else {
            itemMeta.lore(mutableListOf(Component.text("Disabled").color(NamedTextColor.RED)))
        }

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
                    pos.y += 10

                    pos.block.type = Material.LAVA
                }
            }
        }.runTaskTimer(Main.instance, 0, 1)

    }
}