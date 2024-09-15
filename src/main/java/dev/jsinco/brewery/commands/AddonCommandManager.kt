package dev.jsinco.brewery.commands

import com.dre.brewery.BreweryPlugin
import com.dre.brewery.commands.SubCommand
import dev.jsinco.brewery.guis.impl.MainBreweryGui
import dev.jsinco.brewery.utility.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddonCommandManager : SubCommand {

    private val commands: Map<String, AddonSubCommand> = mapOf(
        "edit" to EditCommand(),
        "cancelaction" to CancelActionCommand(),
        "create" to CreateCommand()
    )

    override fun execute(plugin: BreweryPlugin, sender: CommandSender, label: String, rawArgs: Array<out String>) {
        val args = rawArgs.drop(1).toTypedArray()

        if (args.isEmpty()) { // Open gui if args are empty
            val gui = MainBreweryGui()
            gui.initializeGui()
            gui.open(sender as Player)
            return
        }


        val subCommand = commands[args[0]] ?: return

        if (!sender.hasPermission(subCommand.getPermission())) {
            Util.msg(sender, "You do not have permission to execute this command.")
            return
        } else if (subCommand.playerOnly() && sender !is Player) {
            Util.msg(sender, "This command can only be executed by a player.")
            return
        }

        subCommand.execute(sender, args.drop(1).toTypedArray())
    }

    override fun tabComplete(plugin: BreweryPlugin, sender: CommandSender, label: String, rawArgs: Array<out String>): MutableList<String>? {
        val args = rawArgs.drop(1).toTypedArray()
        if (args.size == 1) return commands.keys.toMutableList()
        val subCommand = commands[args[0]] ?: return null
        return subCommand.tabComplete(sender, args)?.toMutableList() ?: return null
    }

    override fun permission(): String {
        return "brewery.admin"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}