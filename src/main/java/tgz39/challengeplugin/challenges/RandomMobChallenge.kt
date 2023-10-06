package tgz39.challengeplugin.challenges

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import tgz39.challengeplugin.Main
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.DefaultChallenge
import java.util.*

object RandomMobChallenge : DefaultChallenge {

    override var isActive = false

    var time = 0
    var delay = nextDelay()

    init {
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
        val lowerBound = Main.instance.config.getInt("challenges.random-mob-challenge.spawn-time-lower-bound")
        val upperBound = Main.instance.config.getInt("challenges.random-mob-challenge.spawn-time-upper-bound")

        return Random().nextInt(lowerBound, upperBound) * 20
    }

    override fun guiItem(): ItemStack {
        val item = ItemStack(Material.SPAWNER, 1)
        item.itemFlags.clear()
        val itemMeta = item.itemMeta
        itemMeta.lore()?.clear()

        itemMeta.displayName(Component.text("Random Mob Challenge").decorate(TextDecoration.BOLD))

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

                if (time >= delay) {
                    val mob = getRandomMob()
                    for (player in Bukkit.getServer().onlinePlayers) {

                        val pos = player.location
                        player.world.spawnEntity(pos, mob)
                        Main.instance.logger.info("spawing " + mob.name)
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