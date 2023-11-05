package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.Challenge
import java.util.*

object RandomMobChallenge : Challenge {

    override var isActive = false
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-mob-challenge.active", value)
            Main.instance.saveConfig()
        }
    var time = 0
    var delay = 0
    var minDelay = 60
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-mob-challenge.min-delay", value)
            Main.instance.saveConfig()
        }
    var maxDelay = 120
        set(value) {
            field = value
            Main.instance.config.set("challenges.random-mob-challenge.max-delay", value)
            Main.instance.saveConfig()
        }

    init {
        val config = Main.instance.config
        isActive = config.getBoolean("challenges.random-mob-challenge.active")
        minDelay = config.getInt("challenges.random-mob-challenge.min-delay")
        maxDelay = config.getInt("challenges.random-mob-challenge.max-delay")

        delay = nextDelay()
        run()
    }

    fun getRandomMob(): EntityType {

        val mobs: MutableList<EntityType> = EntityType.entries.filter { it.isAlive; it.isAlive }.toMutableList()
        val removedMobs = listOf(
            EntityType.EXPERIENCE_ORB,
            EntityType.AREA_EFFECT_CLOUD,
            EntityType.EGG,
            EntityType.LEASH_HITCH,
            EntityType.PAINTING,
            EntityType.ARROW,
            EntityType.SNOWBALL,
            EntityType.FIREBALL,
            EntityType.SMALL_FIREBALL,
            EntityType.ENDER_PEARL,
            EntityType.ENDER_SIGNAL,
            EntityType.THROWN_EXP_BOTTLE,
            EntityType.ITEM_FRAME,
            EntityType.WITHER_SKULL,
            EntityType.PRIMED_TNT,
            EntityType.SPECTRAL_ARROW,
            EntityType.SHULKER_BULLET,
            EntityType.DRAGON_FIREBALL,
            EntityType.ARMOR_STAND,
            EntityType.EVOKER_FANGS,
            EntityType.MINECART_COMMAND,
            EntityType.BOAT,
            EntityType.MINECART,
            EntityType.MINECART_CHEST,
            EntityType.MINECART_FURNACE,
            EntityType.MINECART_TNT,
            EntityType.MINECART_HOPPER,
            EntityType.MINECART_MOB_SPAWNER,
            EntityType.LLAMA_SPIT,
            EntityType.ENDER_CRYSTAL,
            EntityType.TRIDENT,
            EntityType.GLOW_ITEM_FRAME,
            EntityType.MARKER,
            EntityType.CHEST_BOAT,
            EntityType.BLOCK_DISPLAY,
            EntityType.INTERACTION,
            EntityType.ITEM_DISPLAY,
            EntityType.TEXT_DISPLAY,
            EntityType.PLAYER
        )

        for (mob in EntityType.entries) {
            if (mob in removedMobs) {
                mobs.remove(mob)
            }
        }

        return mobs[Random().nextInt(mobs.size)]
    }

    fun nextDelay(): Int {
        return Random().nextInt(minDelay, maxDelay) * 20
    }

    override fun guiItem(): ItemStack {

        val item = ItemStack(Material.ALLAY_SPAWN_EGG, 1)
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore() ?: ArrayList()

        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)

        itemMeta.displayName(
            Component.text("Random Mob Challenge").decorate(TextDecoration.BOLD)
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

        itemMeta.lore(lore)
        item.setItemMeta(itemMeta)

        return item
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {

                if (!isActive) return
                if (!Timer.isActive) return

                if (time >= delay) {
                    val mob = getRandomMob()
                    for (player in Bukkit.getServer().onlinePlayers) {

                        val pos = player.location
                        player.world.spawnEntity(pos, mob)
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