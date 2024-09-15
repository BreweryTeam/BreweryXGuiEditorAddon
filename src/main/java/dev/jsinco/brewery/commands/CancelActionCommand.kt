package dev.jsinco.brewery.commands

import dev.jsinco.brewery.Events
import dev.jsinco.brewery.utility.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CancelActionCommand : AddonSubCommand {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        sender as Player
        val cancelled = Events.stopListeningForNextChat(sender)
        if (cancelled) {
            Util.msg(sender, "Cancelled action.")
        } else {
            Util.msg(sender, "No action to cancel.")
        }
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String>? {
        return null
    }

    override fun getPermission(): String {
        return "brewery.gui.cancelaction"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}