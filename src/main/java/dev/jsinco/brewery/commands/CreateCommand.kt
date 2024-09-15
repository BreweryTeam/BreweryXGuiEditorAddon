package dev.jsinco.brewery.commands

import com.dre.brewery.recipe.BRecipe
import dev.jsinco.brewery.guis.impl.PotionRecipeEditorGui
import dev.jsinco.brewery.utility.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateCommand : AddonSubCommand {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            Util.msg(sender, "You must specify a unique recipe ID.")
            return
        }

        val recipeID = args[0]

        if (BRecipe.getById(recipeID) != null) {
            Util.msg(sender, "A recipe with that ID already exists.")
            return
        }

        val recipe = Util.getDefaultRecipe(recipeID)
        val gui = PotionRecipeEditorGui(recipe)
        gui.initializeGui()
        gui.open(sender as Player)
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String>? {
        return listOf("<recipe ID>")
    }

    override fun getPermission(): String {
        return "brewery.gui.create"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}