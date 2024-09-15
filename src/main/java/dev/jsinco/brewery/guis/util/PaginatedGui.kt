package dev.jsinco.brewery.guis.util

import com.dre.brewery.utility.BUtil
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class PaginatedGui (
    val name: String,
    private val base: Inventory,
    val items: List<ItemStack?>,
    val slots: List<Int>?
) {

    val pages: MutableList<Inventory> = mutableListOf()
    val isEmpty = items.isEmpty()
    var size : Int = 0
        private set


    init {
        var currentPage = newPage()
        var currentItem = 0
        var currentSlot = 0
        while (currentItem < items.size) {
            if (currentSlot == currentPage.size) {
                currentPage = newPage()
                currentSlot = 0
            }

            if ((slots != null && slots.contains(currentSlot)) && currentPage.getItem(currentSlot) == null ||
                slots == null && currentPage.getItem(currentSlot) == null) {
                currentPage.setItem(currentSlot, items[currentItem])
                currentItem++
            }
            currentSlot++
        }
        size = pages.size
    }

    private fun newPage(): Inventory {
        val inventory: Inventory = Bukkit.createInventory(base.holder, base.size, BUtil.color(name))
        for (i in 0 until base.size) {
            inventory.setItem(i, base.getItem(i))
        }
        pages.add(inventory)
        return inventory
    }

    fun getPreviousPage(page: Inventory): Inventory? {
        val index = pages.indexOf(page)
        return if (index - 1 >= 0) pages[index - 1] else null
    }

    fun getNextPage(page: Inventory): Inventory? {
        val index = pages.indexOf(page)
        return if (index + 1 < pages.size) pages[index + 1] else null
    }


    fun getPage(page: Int): Inventory {
        return pages[page]
    }

    fun indexOf(page: Inventory): Int {
        return pages.indexOf(page)
    }

    class Builder {
        private var name: String = "PaginatedGuiBuilder"
        private var base: Inventory? = null
        private var items: List<ItemStack?> = listOf()
        private var slots: List<Int>? = null

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun base(base: Inventory): Builder {
            this.base = base
            return this
        }

        fun items(items: List<ItemStack?>): Builder {
            this.items = items
            return this
        }

        fun slots(slots: List<Int>): Builder {
            this.slots = slots
            return this
        }

        fun build(): PaginatedGui {
            if (base == null) {
                throw IllegalStateException("Base inventory must be set.")
            }
            return PaginatedGui(name, base!!, items, slots)
        }
    }
}