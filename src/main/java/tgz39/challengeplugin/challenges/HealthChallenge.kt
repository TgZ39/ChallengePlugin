package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.utils.DefaultChallenge


object HealthChallenge : DefaultChallenge {

    override var isActive = false

    init {
        updateHealth()
    }

    override fun guiItem(): ItemStack {

        val config = Main.instance.config
        val item = ItemStack(Material.TOTEM_OF_UNDYING, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.displayName(
            Component.text("Health Challenge").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GOLD)
        )

        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))

        }

        lore.add(Component.text(""))
        lore.add(
            Component.text("Health: " + config.get("challenges.health-challenge.health")).decoration(
                TextDecoration.ITALIC, false
            ).color(NamedTextColor.WHITE)
        )

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    fun setHealth(healthPoints: Double) {
        for (player in Bukkit.getServer().onlinePlayers) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = healthPoints
        }
    }

    fun updateHealth() {

        val config = Main.instance.config

        if (config.getBoolean("challenges.health-challenge.active")) {
            setHealth(config.getInt("challenges.health-challenge.health").toDouble())
        } else {
            setHealth(20.0)
        }
    }

}