package dev.jsinco.brewery.commands

import com.dre.brewery.recipe.BRecipe
import com.dre.brewery.utility.Logging
import dev.jsinco.brewery.guis.impl.PotionRecipeEditorGui
import dev.jsinco.brewery.utility.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand : AddonSubCommand {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            Logging.msg(sender, "You must specify a unique recipe ID.")
            return
        }

        val recipeID = args[0]

        if (BRecipe.getById(recipeID) != null) {
            Logging.msg(sender, "A recipe with that ID already exists.")
            return
        }
        val player = if (args.size > 1) Bukkit.getPlayerExact(args[1]) ?: sender as? Player ?: return else sender as? Player ?: return

        val recipe = Util.getDefaultRecipe(recipeID)
        val gui = PotionRecipeEditorGui(recipe, player)
        gui.initializeGui()
        gui.open(player)
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String>? {
        return listOf("<recipe ID>")
    }

    override fun getPermission(): String {
        return "brewery.gui.create"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}