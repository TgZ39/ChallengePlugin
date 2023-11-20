package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.Challenge
import java.util.*

object RandomEffectChallenge : Challenge {

    // Variables to control the Challenge
    override var isActive = false
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.active", value)
            Main.instance.saveConfig()
        }
    var time: Int = 0
    var delay: Int = 0
    var minDelay: Int = 120
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.min-delay", value)
            Main.instance.saveConfig()
        }
    var maxDelay: Int = 180
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.max-delay", value)
            Main.instance.saveConfig()
        }
    var minLevel: Int = 0
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.min-level", value)
            Main.instance.saveConfig()
        }
    var maxLevel: Int = 9
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.max-level", value)
            Main.instance.saveConfig()
        }
    var infiniteEffectDuration: Boolean = false
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.infinite-effect-duration", value)
            Main.instance.saveConfig()
        }
    var effectDuration: Int = 180
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-effect-challenge.effect-duration", value)
            Main.instance.saveConfig()
        }

    // load values from config
    init {
        val config = Main.instance.config
        isActive = config.getBoolean("challenges.random-effect-challenge.active")
        minDelay = config.getInt("challenges.random-effect-challenge.min-delay")
        maxDelay = config.getInt("challenges.random-effect-challenge.max-delay")
        minLevel = config.getInt("challenges.random-effect-challenge.min-level")
        maxLevel = config.getInt("challenges.random-effect-challenge.max-level")
        infiniteEffectDuration = config.getBoolean("challenges.random-effect-challenge.infinite-effect-duration")
        effectDuration = config.getInt("challenges.random-effect-challenge.effect-duration")

        delay = nextDelay()
        run()
    }

    // GUI item for /settings command
    override fun guiItem(): ItemStack {

        val item = ItemStack(Material.POTION, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)

        itemMeta.displayName(
            Component.text("Random Effect Challenge").decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GOLD)
        )

        if (isActive) {
            lore.add(Component.text("Enabled").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))

        } else {
            lore.add(Component.text("Disabled").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
        }

        lore.add(Component.text(""))
        lore.add(
            Component.text(
                "Delay: ${minDelay}s - ${maxDelay}s"
            ).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        lore.add(
            Component.text(
                "Level: ${minLevel + 1} - ${maxLevel + 1}"
            ).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )

        if (!infiniteEffectDuration) {
            lore.add(
                Component.text("Duration: ${effectDuration}s")
                    .color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
        } else {
            lore.add(
                Component.text("Duration: ∞" + "s").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
        }

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    fun nextDelay(): Int {
        return Random().nextInt(minDelay, maxDelay) * 20
    }

    // return random effect
    fun getRandomEffect(): PotionEffectType {

        val effects: MutableList<PotionEffectType> = PotionEffectType.values().toMutableList()
        effects.remove(PotionEffectType.UNLUCK)
        effects.remove(PotionEffectType.HARM)
        effects.remove(PotionEffectType.WITHER)

        return effects[Random().nextInt(effects.size)]
    }

    fun getRandomEffectLevel(): Int {
        return Random().nextInt(minLevel, maxLevel)
    }

    fun effectDuration(): Int {
        return if (infiniteEffectDuration) Int.MAX_VALUE
        else effectDuration * 20
    }

    private fun run() {
        object : BukkitRunnable() {
            // give each player a random effect at random interval
            override fun run() {

                if (!isActive) return
                if (!Timer.isActive) return

                if (time >= delay) {
                    val effectType = getRandomEffect()
                    // create random effect
                    val effect = PotionEffect(
                        effectType,
                        effectDuration(),
                        getRandomEffectLevel(),
                        true
                    )

                    for (player in Bukkit.getServer().onlinePlayers) {
                        if (player.gameMode != GameMode.SURVIVAL) continue
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