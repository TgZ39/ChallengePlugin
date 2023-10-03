package tgz39.challengeplugin.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockGrowEvent

class BlockGrowListener : Listener {

    @EventHandler
    fun onBlockGrow(event: BlockGrowEvent) {
        val block = event.block
        val pos = block.location

        Bukkit.getWorld(block.world.name)?.createExplosion(pos, 40f)
    }

}