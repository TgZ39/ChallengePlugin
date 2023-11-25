package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tgz39.challengeplugin.challenges.RandomItemCraftChallenge
import tgz39.challengeplugin.utils.sendMessage

class MyItemCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage(
                Component.text("You need to be a player to run this command.").color(NamedTextColor.RED)
            )
            return false
        }

        val player: Player = sender

        if (!RandomItemCraftChallenge.isActive) {
            sendMessage(player, "Skip Item", "Random Item Craft Challenge needs to be active.", NamedTextColor.RED)
            return false
        }

        RandomItemCraftChallenge.displayItem(player)
        return false
    }
}