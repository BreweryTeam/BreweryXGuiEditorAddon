package dev.jsinco.brewery

import com.dre.brewery.BreweryPlugin
import dev.jsinco.brewery.guis.AbstractGui
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class Events : Listener {

    companion object {
        val listenForNextChat: MutableMap<UUID, AbstractGui> = mutableMapOf()
        fun listenForNextChat(player: Player, gui: AbstractGui) {
            listenForNextChat[player.uniqueId] = gui
            player.closeInventory()
            player.sendMessage(Component.text("[Click Here to Cancel]").clickEvent(ClickEvent.runCommand("/breweryx gui cancelaction")).color(NamedTextColor.RED))
        }
        fun stopListeningForNextChat(player: Player): Boolean {
            if (!listenForNextChat.containsKey(player.uniqueId)) return false
            listenForNextChat[player.uniqueId]?.open(player)
            listenForNextChat.remove(player.uniqueId)
            return true
        }
    }


    @EventHandler
    fun onInvClick(event: InventoryClickEvent) {
        if (event.inventory.getHolder(false) !is AbstractGui) return
        (event.inventory.holder as AbstractGui).handleClickEvent(event)
    }

    @EventHandler
    fun onInvClose(event: InventoryCloseEvent) {
        if (event.inventory.getHolder(false) !is AbstractGui) return
        (event.inventory.holder as AbstractGui).handleCloseEvent(event)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (!listenForNextChat.containsKey(event.player.uniqueId)) return
        val gui = listenForNextChat[event.player.uniqueId] ?: return
        BreweryPlugin.getScheduler().runTask {
            gui.handleChatEvent(event)
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        listenForNextChat.remove(event.player.uniqueId)
    }
}