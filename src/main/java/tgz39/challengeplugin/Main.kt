package tgz39.challengeplugin

import org.bukkit.plugin.java.JavaPlugin
import tgz39.challengeplugin.commands.ClearWeather
import tgz39.challengeplugin.listener.BlockGrowListener

class Main : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        logger.info("Loading Plugin...")
        getCommand("clearweather")?.setExecutor(ClearWeather())
        server.pluginManager.registerEvents(BlockGrowListener(), this)
    }
    override fun onDisable() {
        // Plugin shutdown logic
    }
}
