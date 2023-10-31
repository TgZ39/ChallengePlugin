package tgz39.challengeplugin.utils

import org.bukkit.inventory.ItemStack

interface Challenge {

    var isActive: Boolean
    fun guiItem(): ItemStack

}