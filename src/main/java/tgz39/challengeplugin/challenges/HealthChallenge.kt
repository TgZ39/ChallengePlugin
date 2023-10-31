package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.utils.Challenge


object HealthChallenge : Challenge {

    override var isActive = false
    var health = 6.0

    init {
        updateConfig()
        updateHealth()
    }

    fun updateConfig() {
        val config = Main.instance.config
        isActive = config.getBoolean("challenges.health-challenge.active")
        health = config.getInt("challenges.health-challenge.health").toDouble()
    }

    override fun guiItem(): ItemStack {
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
            Component.text("Health: ${health.toInt()}").decoration(
                TextDecoration.ITALIC, false
            ).color(NamedTextColor.WHITE)
        )

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    fun setPlayerHealth(healthPoints: Double) {
        for (player in Bukkit.getServer().onlinePlayers) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = healthPoints
            player.health = healthPoints
        }
    }

    fun updateHealth() {
        if (isActive) {
            setPlayerHealth(health)
        } else {
            setPlayerHealth(20.0)
        }
    }

}