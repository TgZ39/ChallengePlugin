package tgz39.challengeplugin

import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import tgz39.challengeplugin.challenges.*
import tgz39.challengeplugin.commands.SettingsCommand
import tgz39.challengeplugin.commands.SkipItemCommand
import tgz39.challengeplugin.commands.TimerCommand
import tgz39.challengeplugin.listeners.DeathLisenter
import tgz39.challengeplugin.listeners.EnderDragonDeath
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

        logger.info("Loading Plugin...")

        initializeChallenges()
        saveDefaultConfig()

        // register commands
        getCommand("settings")?.setExecutor(SettingsCommand())
        getCommand("timer")?.setExecutor(TimerCommand())
        getCommand("timer")?.tabCompleter = TimerCommand()
        getCommand("skipitem")?.setExecutor(SkipItemCommand())

        // register Event Listeners for Challenges
        server.pluginManager.registerEvents(SettingsGUI, this)
        server.pluginManager.registerEvents(DeathLisenter(), this)
        server.pluginManager.registerEvents(EnderDragonDeath(), this)
        server.pluginManager.registerEvents(RandomBlockDropChallenge, this)
        server.pluginManager.registerEvents(RandomItemCollectChallenge, this)

        Timer.sendActionBar()

    }

    override fun onDisable() {
        // Plugin shutdown logic
        Timer.isActive = false
        saveDefaultConfig()
    }

    private fun initializeChallenges() {
        HealthChallenge
        LavaChallenge
        RandomBlockDropChallenge
        RandomEffectChallenge
        RandomItemCollectChallenge
        RandomMobChallenge
    }
}
