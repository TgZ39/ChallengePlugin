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
        lateinit var timer: Timer
        lateinit var settingsGUI: SettingsGUI
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {

        saveDefaultConfig()

        timer = Timer()
        settingsGUI = SettingsGUI()

        // Plugin startup logic
        logger.info("Loading Plugin...")

        getCommand("settings")?.setExecutor(SettingsCommand())
        getCommand("timer")?.setExecutor(TimerCommand())
        getCommand("timer")?.setTabCompleter(TimerCommand())

        server.pluginManager.registerEvents(SettingsGUI(), this)
        server.pluginManager.registerEvents(DeathLisenter(), this)
    }
    override fun onDisable() {
        // Plugin shutdown logic
        saveDefaultConfig()
    }
}
