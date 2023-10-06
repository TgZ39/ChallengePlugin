package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.DefaultChallenge
import java.util.*

object RandomEffectChallenge : DefaultChallenge {

    override var isActive = false
    var time = 0
    var delay = nextDelay()

    init {
        run()
    }

    override fun guiItem(): ItemStack {

        val config = Main.instance.config
        val item = ItemStack(Material.POTION, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)

        itemMeta.displayName(Component.text("Random Effect Challenge").decorate(TextDecoration.BOLD))

        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED))
        }

        lore.add(Component.text(""))
        lore.add(
            Component.text(
                "Delay: " + config.get("challenges.random-effect-challenge.run-time-lower-bound") + "s - " + config.get(
                    "challenges.random-effect-challenge.run-time-upper-bound"
                ) + "s"
            ).color(NamedTextColor.WHITE)
        )
        lore.add(
            Component.text(
                "Level: " + (config.getInt("challenges.random-effect-challenge.min-level") + 1).toString() + " - " + (config.getInt(
                    "challenges.random-effect-challenge.max-level"
                ) + 1).toString()
            ).color(NamedTextColor.WHITE)
        )

        if (config.get("challenges.random-effect-challenge.infinite-effect-duration") == false) {
            lore.add(
                Component.text("Duration: " + config.get("challenges.random-effect-challenge.effect-duration") + "s")
                    .color(NamedTextColor.WHITE)
            )
        } else {
            lore.add(Component.text("Duration: ∞" + "s").color(NamedTextColor.WHITE))
        }

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item


    }

    fun nextDelay(): Int {
        val lowerBound = Main.instance.config.getInt("challenges.random-effect-challenge.run-time-lower-bound")
        val upperBound = Main.instance.config.getInt("challenges.random-effect-challenge.run-time-upper-bound")

        return Random().nextInt(lowerBound, upperBound) * 20
    }

    fun getRandomEffect(): PotionEffectType {

        val effects: MutableList<PotionEffectType> = PotionEffectType.values().toMutableList()
        effects.remove(PotionEffectType.UNLUCK)
        effects.remove(PotionEffectType.HARM)
        effects.remove(PotionEffectType.WITHER)

        return effects[Random().nextInt(effects.size)]
    }

    fun getRandomEffectLevel(): Int {
        val lowerBound = Main.instance.config.getInt("challenges.random-effect-challenge.min-level")
        val upperBound = Main.instance.config.getInt("challenges.random-effect-challenge.max-level")

        return Random().nextInt(lowerBound, upperBound)
    }

    fun effectDuration(): Int {
        if (Main.instance.config.getBoolean("challenges.random-effect-challenge.infinite-effect-duration")) return Int.MAX_VALUE
        else return Main.instance.config.getInt("challenges.random-effect-challenge.effect-duration") * 20
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {

                if (!isActive) return
                if (!Timer.isActive) return

                if (time >= delay) {
                    val effectType = getRandomEffect()
                    val effect = PotionEffect(
                        effectType,
                        effectDuration(),
                        getRandomEffectLevel(),
                        true
                    )

                    for (player in Bukkit.getServer().onlinePlayers) {
                        player.addPotionEffect(effect)
                    }

                    time = 0
                    delay = nextDelay()

                } else {
                    time++
                }


            }
        }.runTaskTimer(Main.instance, 0, 1)
    }

}