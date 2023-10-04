package tgz39.challengeplugin.utils

import org.bukkit.inventory.ItemStack

interface DefaultChallenge {

    fun guiItem(): ItemStack
    var isActive: Boolean

}