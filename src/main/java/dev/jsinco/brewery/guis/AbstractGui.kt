package dev.jsinco.brewery.guis

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class AbstractGui : InventoryHolder {

    abstract fun initializeGui()

    abstract fun handleClickEvent(event: InventoryClickEvent)

    abstract fun open(player: Player)

    open fun handleCloseEvent(event: InventoryCloseEvent) {}

    open fun handleChatEvent(event: AsyncPlayerChatEvent) {}

    override fun getInventory(): Inventory {
        TODO()
    }
}