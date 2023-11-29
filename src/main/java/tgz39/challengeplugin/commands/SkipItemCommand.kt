package tgz39.challengeplugin.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tgz39.challengeplugin.challenges.RandomItemCollectChallenge
import tgz39.challengeplugin.utils.sendMessage

class SkipItemCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender !is Player) {
            sender.sendMessage(
                Component.text("You need to be a player to run this command.").color(NamedTextColor.RED)
            )
            return false
        }

        val player: Player = sender

        if (!RandomItemCollectChallenge.isActive) {
            sendMessage(player, "Skip Item", "Random Item Craft Challenge needs to be active.", NamedTextColor.RED)
            return false
        }


        if (RandomItemCollectChallenge.playerSkipCount[player]!! <= 0) {
            sendMessage(player, "Skip Item", "You dont have any skips left.", NamedTextColor.RED)
            return false
        } else {
            RandomItemCollectChallenge.playerItemCount[player] =
                RandomItemCollectChallenge.playerItemCount[player]!! + 1
            RandomItemCollectChallenge.playerSkipCount[player] =
                RandomItemCollectChallenge.playerSkipCount[player]!! - 1

            sendMessage(
                player,
                "Skip Item",
                "You skipped your item. You have ${RandomItemCollectChallenge.playerSkipCount[player]} skips left.",
                NamedTextColor.WHITE
            )
            return false
        }
    }
}