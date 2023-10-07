package tgz39.challengeplugin

import org.bukkit.plugin.java.JavaPlugin
import tgz39.challengeplugin.commands.SettingsCommand
import tgz39.challengeplugin.commands.TimerCommand
import tgz39.challengeplugin.listeners.DeathLisenter
import tgz39.challengeplugin.timer.Timer
import tgz39.challengeplugin.utils.SettingsGUI

class Main : JavaPlugin() {

    companion object {
        lateinit var instance: Main
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {

        saveDefaultConfig()
        logger.info("Loading Plugin...")

        getCommand("settings")?.setExecutor(SettingsCommand())
        getCommand("timer")?.setExecutor(TimerCommand())
        getCommand("timer")?.tabCompleter = TimerCommand()

        server.pluginManager.registerEvents(SettingsGUI, this)
        server.pluginManager.registerEvents(DeathLisenter(), this)

        Timer.sendActionBar()

        logger.info("Finished loading Plugin.")
        logger.info("yes ")
        // fixing code
    }

    override fun onDisable() {
        // Plugin shutdown logic
        Timer.isActive = false
        saveDefaultConfig()
    }
}
