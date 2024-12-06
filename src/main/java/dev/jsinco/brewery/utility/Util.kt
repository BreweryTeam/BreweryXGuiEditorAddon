package dev.jsinco.brewery.utility

import com.dre.brewery.BarrelWoodType
import com.dre.brewery.recipe.BRecipe
import com.dre.brewery.utility.BUtil
import com.dre.brewery.utility.Tuple
import dev.jsinco.brewery.GuiEditorAddon
import java.text.SimpleDateFormat
import java.util.Date

object Util {

    @JvmStatic
    fun colorList(m: List<String>): List<String> {
        return m.map { BUtil.color(it) }
    }

    fun log(m: String) {
        GuiEditorAddon.getLogger().info(m)
    }


    // Something that REALLY needs to get done in BreweryX is swapping internal wood types from numbers to just strings.
    // I don't know what idiot dev thought it was a good idea to use ints for wood types, but now it's a problem.
    // wood: Wood of the barrel 0=any 1=Birch 2=Oak 3=Jungle 4=Spruce 5=Acacia 6=Dark Oak 7=Crimson 8=Warped 9=Mangrove 10=Cherry 11=Bamboo (12=Cut Copper)
    fun stringToWoodType(woodType: String?): Int {
        if (woodType == null) {
            return 0
        }
        try {
            return woodType.toInt()
        } catch (ignored: NumberFormatException) {
        }

        return when (woodType.lowercase()) {
            "birch" -> 1
            "oak" -> 2
            "jungle" -> 3
            "spruce" -> 4
            "acacia" -> 5
            "dark_oak" -> 6
            "crimson" -> 7
            "warped" -> 8
            "mangrove" -> 9
            "cherry" -> 10
            "bamboo" -> 11
            "cut_copper" -> 12
            else -> 0
        }
    }

    @JvmStatic
    fun woodTypeToString(woodType: Int): String {
        return when (woodType) {
            1 -> "Birch"
            2 -> "Oak"
            3 -> "Jungle"
            4 -> "Spruce"
            5 -> "Acacia"
            6 -> "Dark Oak"
            7 -> "Crimson"
            8 -> "Warped"
            9 -> "Mangrove"
            10 -> "Cherry"
            11 -> "Bamboo"
            12 -> "Cut Copper"
            else -> "Any"
        }
    }

    fun getConfigStringBasedOnQuality(values: List<Tuple<Int, String>>?): List<String>? {
        if (values == null) return null
        val list: MutableList<String> = mutableListOf()
        for (value in values) {
            when (value.first()) {
                0 -> list.add(value.second())
                1 -> list.add("+ ${value.second()}")
                2 -> list.add("++ ${value.second()}")
                3 -> list.add("+++ ${value.second()}")
            }
        }
        return list
    }

    fun <T> getOrReturnFirstElement(args: List<T>, index: Int): T? {
        return if (args.size > index) args[index] else args.firstOrNull()
    }

    fun <T> getOrReturnFirstElement(args: Array<out T>, index: Int): T? {
        return if (args.size > index) args[index] else args.firstOrNull()
    }

    fun getOrReturnFirstElement(args: IntArray, index: Int): Int? {
        return if (args.size > index) args[index] else args.firstOrNull()
    }

    fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return formatter.format(date)
    }

    fun getDefaultRecipe(id: String): BRecipe {
        return BRecipe.Builder("Unnamed Potion")
            .setID(id)
            .addIngredient(BRecipe.loadIngredients(listOf("wheat/1"), id)[0])
            .cook(5)
            .distill(0, 40)
            .age(0, BarrelWoodType.ANY)
            .difficulty(1)
            .get()
    }

    fun formatMaterialName(s: String): String {
        var name = s.lowercase().replace("_", " ")
        name = name.substring(0, 1).uppercase() + name.substring(1)
        for (i in name.indices) {
            if (name[i] == ' ') {
                name =
                    name.substring(0, i) + " " + name[i + 1].toString().uppercase() + name.substring(
                        i + 2
                    ) // Capitalize first letter of each word
            }
        }
        return name
    }
}