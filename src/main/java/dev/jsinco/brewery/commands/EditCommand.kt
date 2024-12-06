package dev.jsinco.brewery.commands

import com.dre.brewery.commands.CommandUtil
import com.dre.brewery.recipe.BRecipe
import com.dre.brewery.utility.Logging
import dev.jsinco.brewery.guis.impl.PotionRecipeEditorGui
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EditCommand : AddonSubCommand {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            Logging.msg(sender, "&cPlease specify a recipe.")
            return
        }

        val recipe = BRecipe.getMatching(args[0]) ?: return Logging.msg(sender, "&cRecipe not found.")

        val player = if (args.size > 1) {
            Bukkit.getPlayerExact(args[1]) ?: return Logging.msg(sender, "&cPlayer not found.")
        } else {
            sender as? Player ?: return Logging.msg(sender, "&cYou must be a player to use this command.")
        }

        val gui = PotionRecipeEditorGui(recipe, player)
        gui.initializeGui()
        gui.open(player)
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String>? {
        return CommandUtil.recipeNamesAndIds(args)
    }

    override fun getPermission(): String {
        return "brewery.gui.edit"
    }

    override fun playerOnly(): Boolean {
        return false
    }

}