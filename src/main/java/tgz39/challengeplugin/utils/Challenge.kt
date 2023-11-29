package tgz39.challengeplugin.utils

import org.bukkit.inventory.ItemStack
import tgz39.challengeplugin.timer.Timer

abstract class Challenge {

    init {
        ChallengeAction.addChallenge(this)
    }

    abstract var isActive: Boolean
    abstract fun guiItem(): ItemStack

    open fun onEnable() {}
    open fun onDisable() {}
}

object ChallengeAction {
    val challenges: MutableList<Challenge> = mutableListOf()

    fun enable() {
        challenges.forEach {
            if (it.isActive && Timer.isActive) it.onEnable()
        }
    }

    fun disable() {
        challenges.forEach {
            if (!it.isActive || !Timer.isActive) it.onDisable()
        }
    }

    fun addChallenge(challenge: Challenge) {
        challenges.add(challenge)
    }
}