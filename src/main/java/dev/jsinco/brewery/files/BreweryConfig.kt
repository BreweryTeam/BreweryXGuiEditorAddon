package dev.jsinco.brewery.files

import com.dre.brewery.BreweryPlugin
import com.dre.brewery.recipe.BRecipe
import dev.jsinco.brewery.utility.Util

@Deprecated("BreweryX's configuration system has changed")
object BreweryConfig {

    private val breweryInstance = BreweryPlugin.getInstance()

    fun saveRecipeToConfig(recipe: BRecipe, editor: String = "Unknown") {
        val path = "recipes.${recipe.id}"

        val section = breweryInstance.config.getConfigurationSection(path)
            ?: breweryInstance.config.createSection(path)

        section.set("name", recipe.name.joinToString("/"))
        section.set("ingredients", recipe.ingredients.map { it.toConfigString() })
        section.set("cookingtime", recipe.cookingTime)
        section.set("distillruns", recipe.distillruns)
        section.set("distilltime", recipe.distillTime)
        section.set("wood", recipe.wood)
        section.set("age", recipe.age.toInt())
        section.set("color", Integer.toHexString(recipe.color.color.asRGB()))
        section.set("difficulty", recipe.difficulty)
        section.set("alcohol", recipe.alcohol)
        if (recipe.isGlint) section.set("glint", true)
        if (recipe.cmData != null && recipe.cmData!!.isNotEmpty()) section.set("customModelData", recipe.cmData.joinToString("/"))
        recipe.effects?.let { section.set("effects", it.map {thing -> thing.toString() }) }
        recipe.lore?.let { section.set("lore", Util.getConfigStringBasedOnQuality(it)) }
        recipe.servercmds?.let { section.set("servercommands", Util.getConfigStringBasedOnQuality(it)) }
        recipe.playercmds?.let { section.set("playercommands", Util.getConfigStringBasedOnQuality(it)) }
        recipe.drinkMsg?.let { section.set("drinkmessage", it) }
        recipe.drinkTitle?.let { section.set("drinktitle", it) }


        breweryInstance.config.setComments(path, listOf(
            "This recipe was last edited by $editor using",
            "BreweryXGuiEditorAddon on ${Util.getCurrentDate()}",
        ))
        breweryInstance.saveConfig()
        Util.log("Saved recipe ${recipe.id} to config")
    }
}