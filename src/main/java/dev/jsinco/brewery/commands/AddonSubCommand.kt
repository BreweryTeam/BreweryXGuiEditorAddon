package dev.jsinco.brewery.commands

import org.bukkit.command.CommandSender

interface AddonSubCommand {
    fun execute(sender: CommandSender, args: Array<out String>)

    fun tabComplete(sender: CommandSender, args: Array<out String>): List<String>?

    fun getPermission(): String

    fun playerOnly(): Boolean
}