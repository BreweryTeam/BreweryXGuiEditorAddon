package dev.jsinco.brewery.guis.util

import com.dre.brewery.recipe.BRecipe
import com.dre.brewery.utility.BUtil
import dev.jsinco.brewery.utility.Util
import dev.jsinco.brewery.utility.Util.colorList
import dev.jsinco.brewery.utility.Util.woodTypeToString
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta

object BRecipeUtil {

    private fun combinedList(playerCommands: Map<Int, String>?, serverCommands: Map<Int, String>?): List<String> {
        val combined: MutableList<String> = ArrayList()
        if (playerCommands != null) combined.addAll(playerCommands.values.stream().map { s: String -> "player:/$s" }
            .toList())
        if (serverCommands != null) combined.addAll(serverCommands.values.stream().map { s: String -> "server:/$s" }
            .toList())
        return combined
    }


    fun bRecipeToItemStack(recipe: BRecipe): ItemStack {
        val itemStack = ItemStack(Material.POTION)
        val meta = itemStack.itemMeta as PotionMeta

        meta.setDisplayName(BUtil.color("&f${recipe.name.joinToString(", ")}"))

        if (recipe.cmData != null && recipe.cmData.isNotEmpty()) {
            meta.setCustomModelData(recipe.cmData.random())
        }

        if (recipe.hasGlint()) {
            meta.addEnchant(Enchantment.MENDING, 1, true)
        }

        meta.color = recipe.color.color
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_ENCHANTS)

        val description: MutableList<String> = mutableListOf()

        description.addAll(
            listOf(
                //"",  // space
                "&#E59FE3Difficulty&7: &f${recipe.difficulty}",
                "&#E59FE3Cook Time&7: &f${recipe.cookingTime}",
                "&#E59FE3Distill Runs&7: &f${recipe.distillRuns}",
                "&#E59FE3Distill Time&7: &f${recipe.distillTime}",
                "&#E59FE3Barrel Type&7: &f${woodTypeToString(recipe.wood.toInt())}",
                "&#E59FE3Aging Amount&7: &f${recipe.age.toInt()}",
                "&#E59FE3Alcohol&7: &f${recipe.alcohol}",
                "&#E59FE3Glint&7: &f${recipe.hasGlint()}",
            )
        )


        description.add("&#E59FE3Ingredients&7:")
        recipe.ingredients.map {
            description.add("&#DD73B7${Util.formatMaterialName(it.toConfigString())}")
        }
        description.add("&#E59FE3Effects&7:")
        recipe.effects.map {
            description.add("&#DD73B7${Util.formatMaterialName(it.toString())}")
        }
        description.add("&#E59FE3Commands&7:")
        combinedList(recipe.playercmds, recipe.servercmds).map {
            description.add("&#DD73B7$it")
        }

        if (recipe.hasLore()) {
            description.add("&#E59FE3Lore&7:")
            description.addAll(recipe.lore!!.values.map { "&9$it" })
        }


        meta.lore = colorList(description)
        itemStack.setItemMeta(meta)
        return itemStack
    }
}
