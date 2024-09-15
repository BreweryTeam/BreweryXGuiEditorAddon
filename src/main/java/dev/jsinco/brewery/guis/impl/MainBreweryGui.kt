package dev.jsinco.brewery.guis.impl

import com.dre.brewery.BreweryPlugin
import com.dre.brewery.recipe.BRecipe
import dev.jsinco.brewery.Events
import dev.jsinco.brewery.guis.AbstractGui
import dev.jsinco.brewery.guis.util.BRecipeUtil
import dev.jsinco.brewery.guis.util.ItemType
import dev.jsinco.brewery.guis.util.PaginatedGui
import dev.jsinco.brewery.utility.Util
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.persistence.PersistentDataType

/**
 * The root of all Guis in this addon. It's the addon players
 * see when opening our gui and leads to all other guis/editors.
 */
class MainBreweryGui : AbstractGui() {

    companion object {
        val paginatedGuiSlots = listOf(19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34)
        const val PREVIOUS_PAGE_SLOT = 48
        const val NEXT_PAGE_SLOT = 50
        const val GUI_SIZE = 54
        const val GUI_TITLE = "&#906DE3&lBreweryX Recipe Editor"
    }


    lateinit var paginatedGui: PaginatedGui

    override fun initializeGui() {
        val items = BRecipe.getAllRecipes().map { ItemType.MAIN_EDITABLE_POTION_RECIPE.getItemForItemStack(BRecipeUtil.bRecipeToItemStack(it), it.id) }

        val inventory = Bukkit.createInventory(this, GUI_SIZE, "BreweryXGuiEditorAddon")

        inventory.setItem(PREVIOUS_PAGE_SLOT, ItemType.ANY_PREVIOUS_PAGE.item)
        inventory.setItem(NEXT_PAGE_SLOT, ItemType.ANY_NEXT_PAGE.item)
        val createPotion = ItemType.MAIN_CREATE_POTION_RECIPE
        inventory.setItem(createPotion.slot!!, createPotion.item)

        paginatedGui = PaginatedGui(GUI_TITLE, inventory, items, paginatedGuiSlots)
    }

    override fun handleClickEvent(event: InventoryClickEvent) {
        val clickedItem = event.currentItem ?: return
        event.isCancelled = true

        val itemType = ItemType.getItemType(clickedItem) ?: return
        val inv = event.inventory
        val player = event.whoClicked as Player

        when (itemType) {
            ItemType.MAIN_EDITABLE_POTION_RECIPE -> {
                val recipeId = clickedItem.itemMeta.persistentDataContainer.get(NamespacedKey(BreweryPlugin.getInstance(), "data"), PersistentDataType.STRING) ?: return
                val gui = PotionRecipeEditorGui(BRecipe.getMatching(recipeId) ?: return)
                gui.initializeGui()
                gui.open(player)
            }

            ItemType.ANY_PREVIOUS_PAGE -> {
                paginatedGui.getPreviousPage(inv)?.let { event.whoClicked.openInventory(it) }
            }

            ItemType.ANY_NEXT_PAGE -> {
                paginatedGui.getNextPage(inv)?.let { event.whoClicked.openInventory(it) }
            }

            ItemType.MAIN_CREATE_POTION_RECIPE -> {
                Util.msg(player, "Enter a new name for this recipe in chat.")
                Events.listenForNextChat(player, this)
            }

            else -> throw IllegalArgumentException("Unknown ItemType: $itemType")
        }
    }

    override fun handleChatEvent(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message
        val recipeID = message.trim()

        if (recipeID.isEmpty()) {
            Util.msg(player, "Invalid name.")
            return
        } else if (BRecipe.getById(recipeID) != null) {
            Util.msg(player, "A recipe with that ID already exists.")
            return
        }

        // Stupid way to write a builder, but I didn't write it. Probably needs to be changed.
        val recipe = Util.getDefaultRecipe(recipeID)
        val gui = PotionRecipeEditorGui(recipe)
        gui.initializeGui()
        gui.open(player)
    }

    override fun open(player: Player) {
        if (paginatedGui.isEmpty) {
            Util.msg(player, "No recipes found.")
            return
        }
        player.openInventory(paginatedGui.getPage(0))
    }


}