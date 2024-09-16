package dev.jsinco.brewery.guis.impl

import com.dre.brewery.recipe.BEffect
import com.dre.brewery.recipe.BRecipe
import com.dre.brewery.recipe.PotionColor
import com.dre.brewery.utility.BUtil
import com.dre.brewery.utility.StringParser
import dev.jsinco.brewery.Events
import dev.jsinco.brewery.files.BreweryConfig
import dev.jsinco.brewery.guis.AbstractGui
import dev.jsinco.brewery.guis.util.BRecipeUtil
import dev.jsinco.brewery.guis.util.ItemType
import dev.jsinco.brewery.utility.Util
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent

class PotionRecipeEditorGui(recipe: BRecipe, val opener: Player) : AbstractGui() {

    companion object {
        const val GUI_SIZE = 54
        const val GUI_TITLE = "&#906DE3&lBreweryX Recipe Editor"
        const val DISPLAYABLE_POTION_SLOT = 22
    }


    val recipe = recipe.clone()
    val inv = Bukkit.createInventory(this, GUI_SIZE, BUtil.color(GUI_TITLE))
    var listeningForAction: ItemType? = null

    fun startListeningForAction(player: Player, itemType: ItemType, prompt: String) {
        listeningForAction = itemType
        Util.msg(player, prompt)
        Events.listenForNextChat(player, this)
    }

    fun stopListeningForAction(player: Player) {
        listeningForAction = null
        Events.stopListeningForNextChat(player)
    }

    fun updateDisplayablePotion() {
        inv.setItem(DISPLAYABLE_POTION_SLOT, ItemType.EDITOR_DISPLAY_POTION.getItemForItemStack(BRecipeUtil.bRecipeToItemStack(recipe)))
    }


    fun updateIntBasedOnClick(clickType: ClickType, intCopy: Int): Int? {
        var int = intCopy
        return when (clickType) {
            ClickType.LEFT -> {int += 1; int}
            ClickType.DOUBLE_CLICK -> {int += 2; int}
            ClickType.SHIFT_LEFT -> {int += 10; int}
            ClickType.RIGHT -> {int -= 1; int}
            ClickType.SHIFT_RIGHT -> {int -= 10; int}
            else -> null
        }
    }

    fun splitMessageAsList(message: String): List<String> {
        return message.replace("; ", ";").replace(" ;", ";").replace(" ; ", ";").split(";")
    }


    override fun initializeGui() {
        for (itemType in ItemType.values()) {
            var finalItemType = itemType
            if (!itemType.name.startsWith("EDITOR_")) continue
            else if (!opener.hasPermission(itemType.perm)) {
                finalItemType = ItemType.EDITOR_NO_PERMISSION_ITEM
            }
            inv.setItem(itemType.slot ?: continue, finalItemType.item)
        }
        this.updateDisplayablePotion()
    }

    override fun handleClickEvent(event: InventoryClickEvent) {
        val clickedItem = event.currentItem ?: return
        event.isCancelled = true

        val itemType = ItemType.getItemType(clickedItem) ?: return
        val player = event.whoClicked as Player


        when (itemType) {

            ItemType.EDITOR_NO_PERMISSION_ITEM -> {
                Util.msg(player, "You do not have permission to edit this attribute.")
            }

            ItemType.EDITOR_CANCEL -> {
                player.closeInventory()
                Util.msg(player, "Cancelled editing recipe.")
            }

            ItemType.EDITOR_CONFIRM -> {
                val recipes = BRecipe.getAllRecipes()
                val index: Int? = recipes.find { it.id == recipe.id }?.let { recipes.indexOf(it) }
                if (index != null) {
                    recipes[index] = recipe
                } else {
                    recipes.add(recipe)
                }
                // TODO: needs to save to BreweryX config
                player.closeInventory()
                BreweryConfig.saveRecipeToConfig(recipe, player.name)
                Util.msg(player, "Saved recipe! &8(${recipe.id})")
            }


            ItemType.EDITOR_NAME -> {
                this.startListeningForAction(player, itemType, "Enter a new name for this recipe. Separate qualities with a &#B8FDAD'/' character.")
            }

            ItemType.EDITOR_LORE -> {
                this.startListeningForAction(player, itemType,
                    "Enter a new lore for this recipe. Separate lines of lore with a &#B8FDAD';' and qualities with '+', '++', or '+++'. (\"This is my lore; + Appears when quality is bad; ++ Appears when quality is ok; +++ Appears when quality is good).")
            }

            ItemType.EDITOR_DIFFICULTY -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.difficulty)
                if (newValue != null) {
                    recipe.difficulty = newValue.coerceIn(1, 10)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new difficulty for this recipe between &#B8FDAD1-10.")
                }
            }

            ItemType.EDITOR_COOK_TIME -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.cookingTime)
                if (newValue != null) {
                    recipe.cookingTime = newValue.coerceAtLeast(0)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new cooking time for this recipe in &#B8FDADminutes.")
                }
            }

            ItemType.EDITOR_DISTILL_RUNS -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.distillRuns.toInt())
                if (newValue != null) {
                    recipe.distillRuns = newValue.toByte().coerceAtLeast(0)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new number of distill runs for this recipe between &#B8FDAD0-127.")
                }
            }

            ItemType.EDITOR_DISTILL_TIME -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.distillTime)
                if (newValue != null) {
                    recipe.distillTime = newValue.coerceAtLeast(0)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new distillation time for this recipe in &#B8FDADseconds.")
                }
            }

            ItemType.EDITOR_WOOD_TYPE -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.wood.toInt())
                if (newValue != null) {
                    recipe.wood = newValue.toByte().coerceIn(0, 12)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new wood type for this recipe &#B8FDADany, birch, oak....")
                }
            }

            ItemType.EDITOR_AGE -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.age.toInt())
                if (newValue != null) {
                    recipe.setAge(newValue.coerceAtLeast(0))
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new age for this recipe. &#B8FDAD(a number)")
                }
            }

            ItemType.EDITOR_ALCOHOL -> {
                val newValue = updateIntBasedOnClick(event.click, recipe.alcohol)
                if (newValue != null) {
                    recipe.alcohol = newValue.coerceAtLeast(0)
                    this.updateDisplayablePotion()
                } else {
                    this.startListeningForAction(player, itemType, "Enter a new alcohol content for this recipe. &#B8FDAD(a number)")
                }
            }

            ItemType.EDITOR_INGREDIENTS -> {
                this.startListeningForAction(player, itemType,
                    "Enter new ingredients for this recipe. Separate ingredients with a &#B8FDAD';'. (\"diamond/1; bedrock/2; brewery:wheatbeer/1; oraxen:gemstone/4\").")
            }

            ItemType.EDITOR_EFFECTS -> {
                this.startListeningForAction(player, itemType,
                    "Enter new effects for this recipe. Separate effects with a &#B8FDAD';'</#B8FDAD> and qualities with '+', '++', or '+++'. (\"SPEED/1; + HASTE/2-4; ++ STRENGTH/4-10\").")
            }

            ItemType.EDITOR_PLAYER_COMMANDS -> {
                this.startListeningForAction(player, itemType,
                    "Enter new player commands for this recipe. Separate commands with a &#B8FDAD';' and qualities with <#B8FDAD>'+', '++', or '+++'. (\"give @s minecraft:diamond/1; ++ effect give @s minecraft:speed 100 1\").")
            }

            ItemType.EDITOR_SERVER_COMMANDS -> {
                this.startListeningForAction(player, itemType,
                    "Enter new server commands for this recipe. Separate commands with a &#B8FDAD';' and qualities with '+', '++', or '+++'. (\"give @a minecraft:diamond/1; ++ effect give @a minecraft:speed 100 1\").")
            }

            ItemType.EDITOR_GLINT -> {
                recipe.isGlint = !recipe.isGlint
                this.updateDisplayablePotion()
            }

            ItemType.EDITOR_CUSTOM_MODEL_DATA -> {
                this.startListeningForAction(player, itemType, "Enter a new custom model data for this recipe. Separate values with a &#B8FDAD'/' character.")
            }

            ItemType.EDITOR_COLOR -> {
                this.startListeningForAction(player, itemType, "Enter a new color for this recipe. &#B8FDAD(DARK_RED or #FF0000)")
            }

            ItemType.EDITOR_DRINK_MESSAGE -> {
                this.startListeningForAction(player, itemType, "Enter a new drink message for this recipe separate qualities with &#B8FDAD'+', '++', or '+++'. (Drink up!)")
            }

            ItemType.EDITOR_DRINK_TITLE -> {
                this.startListeningForAction(player, itemType, "Enter a new drink title for this recipe separate qualities with &#B8FDAD'+', '++', or '+++'. (Drink up!)")
            }

            else -> throw IllegalArgumentException("Unhandled item type: $itemType")
        }
    }

    override fun handleChatEvent(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message
        val itemType = listeningForAction ?: return

        when (itemType) {
            ItemType.EDITOR_NAME -> {
                println("message: $message")
                recipe.name = message.split("/").toTypedArray()
            }

            ItemType.EDITOR_LORE -> {
                recipe.lore = BRecipe.loadQualityStringList(splitMessageAsList(message), StringParser.ParseType.LORE)
            }

            ItemType.EDITOR_DIFFICULTY -> {
                recipe.difficulty = message.toIntOrNull() ?: return
            }

            ItemType.EDITOR_COOK_TIME -> {
                recipe.cookingTime = message.toIntOrNull() ?: return
            }

            ItemType.EDITOR_DISTILL_RUNS -> {
                recipe.distillRuns = message.toByteOrNull() ?: return
            }

            ItemType.EDITOR_DISTILL_TIME -> {
                recipe.distillTime = message.toIntOrNull() ?: return
            }

            ItemType.EDITOR_WOOD_TYPE -> {
                recipe.wood = Util.stringToWoodType(message).toByte()
            }

            ItemType.EDITOR_AGE -> {
                recipe.setAge(message.toIntOrNull() ?: return)
            }

            ItemType.EDITOR_ALCOHOL -> {
                recipe.alcohol = message.toIntOrNull() ?: return
            }

            ItemType.EDITOR_INGREDIENTS -> {
                recipe.ingredients = BRecipe.loadIngredients(splitMessageAsList(message), recipe.id)
            }

            ItemType.EDITOR_EFFECTS -> {
                recipe.effects = splitMessageAsList(message).map { BEffect(it) }
            }

            ItemType.EDITOR_PLAYER_COMMANDS -> {
                recipe.playercmds = BRecipe.loadQualityStringList(splitMessageAsList(message), StringParser.ParseType.CMD)
            }

            ItemType.EDITOR_SERVER_COMMANDS -> {
                recipe.servercmds = BRecipe.loadQualityStringList(splitMessageAsList(message), StringParser.ParseType.CMD)
            }

            ItemType.EDITOR_CUSTOM_MODEL_DATA -> {
                recipe.cmData = message.split("/").map { it.toInt() }.toIntArray()
            }

            ItemType.EDITOR_COLOR -> {
                recipe.color = PotionColor.fromString(message)
            }

            ItemType.EDITOR_DRINK_MESSAGE -> {
                recipe.drinkMsg = BUtil.color(message)
            }

            ItemType.EDITOR_DRINK_TITLE -> {
                recipe.drinkTitle = BUtil.color(message)
            }

            else -> throw IllegalArgumentException("Unhandled item type: $itemType")
        }

        this.stopListeningForAction(player)
        this.updateDisplayablePotion()
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

}
/*
fun updateItem(itemType: ItemType, value: Any) {
        val item = itemType.item
        val meta = item?.itemMeta ?: return

        meta.setDisplayName(BUtil.color(itemType.displayName?.let { String.format(it, value) }))
        item.itemMeta = meta
        inv.setItem(itemType.slot ?: return, item)
    }
 */