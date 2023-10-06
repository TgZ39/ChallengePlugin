package tgz39.challengeplugin.challenges

import org.bukkit.entity.EntityCategory
import org.bukkit.entity.EntityType
import kotlin.random.Random

object RandomMobChallenge{

    fun getRandomMob(): EntityType {

        val random = Random
        val livingEntity = EntityType.entries.filter { it.isAlive; it.isSpawnable }

        return EntityType.entries[random.nextInt(EntityType.entries.size-1)]

    }

}