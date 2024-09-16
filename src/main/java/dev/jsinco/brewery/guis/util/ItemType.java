package dev.jsinco.brewery.guis.util;

import com.dre.brewery.BreweryPlugin;
import com.dre.brewery.utility.BUtil;
import dev.jsinco.brewery.utility.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemType {

    private static final List<String> quickModLore = Arrays.asList(
            "&7Left-click to increase this value by &61",
            "&7Right-click to decrease this value by &61",
            "&7Shift left-click to increase this value by &610",
            "&7Shift right-click to decrease this value by &610",
            "&7Middle click to enter a value &6manually");

    private static final List<String> modLore = List.of("&7Click to modify this value");


    public static final ItemType ANY_NEXT_PAGE = new ItemType(null, "&#B8FDADNext Page", Material.ARROW);
    public static final ItemType ANY_PREVIOUS_PAGE = new ItemType(null, "&#B8FDADPrevious Page", Material.ARROW);

    // A potion that can be clicked on an opens up an editor for it.
    public static final ItemType MAIN_EDITABLE_POTION_RECIPE = new ItemType();
    public static final ItemType MAIN_CREATE_POTION_RECIPE = new ItemType(49, "&#906DE3Create New Recipe", Material.NETHER_STAR, "&7Click to create a new recipe");

    // For the editor gui
    public static final ItemType EDITOR_NO_PERMISSION_ITEM = new ItemType(null, "&c&lNo Permission", Material.BARRIER, "&7You do not have permission to edit this this attribute.");
    public static final ItemType EDITOR_CANCEL = new ItemType(0, "&c&lCancel", Material.REDSTONE, "&7Click to cancel your edits");
    public static final ItemType EDITOR_CONFIRM = new ItemType(8, "&a&lConfirm", Material.EMERALD, "&7Click to confirm your edits");

    public static final ItemType EDITOR_DISPLAY_POTION = new ItemType();

    public static final ItemType EDITOR_NAME = new ItemType(36, "&#87F5A0&lEdit Name", Material.NAME_TAG, modLore);
    public static final ItemType EDITOR_LORE = new ItemType(37, "&#87F5A0&lEdit Lore", Material.BOOK, modLore);
    public static final ItemType EDITOR_DIFFICULTY = new ItemType(38, "&#87F5A0&lEdit Difficulty", Material.DIAMOND, quickModLore);
    public static final ItemType EDITOR_COOK_TIME = new ItemType(39, "&#87F5A0&lEdit Cook Time", Material.CLOCK, quickModLore);
    public static final ItemType EDITOR_DISTILL_RUNS = new ItemType(40, "&#87F5A0&lEdit Distill Runs", Material.BREWING_STAND, quickModLore);
    public static final ItemType EDITOR_DISTILL_TIME = new ItemType(41, "&#87F5A0&lEdit Distill Time", Material.CLOCK, quickModLore);
    public static final ItemType EDITOR_WOOD_TYPE = new ItemType(42, "&#87F5A0&lEdit Wood Type", Material.OAK_WOOD, quickModLore);
    public static final ItemType EDITOR_AGE = new ItemType(43, "&#87F5A0&lEdit Age", Material.BARREL, quickModLore);
    public static final ItemType EDITOR_ALCOHOL = new ItemType(44, "&#87F5A0&lEdit Alcohol", Material.GLASS_BOTTLE, quickModLore);
    public static final ItemType EDITOR_INGREDIENTS = new ItemType(45, "&#87F5A0&lEdit Ingredients", Material.CHEST, modLore);
    public static final ItemType EDITOR_EFFECTS = new ItemType(46, "&#87F5A0&lEdit Effects", Material.POTION, modLore);
    public static final ItemType EDITOR_PLAYER_COMMANDS = new ItemType(47, "&#87F5A0&lEdit Player Commands", Material.COMMAND_BLOCK, modLore);
    public static final ItemType EDITOR_SERVER_COMMANDS = new ItemType(48, "&#87F5A0&lEdit Server Commands", Material.CHAIN_COMMAND_BLOCK, modLore);
    public static final ItemType EDITOR_GLINT = new ItemType(49, "&#87F5A0&lToggle Glint", Material.ENCHANTING_TABLE, "&7Click to toggle");
    public static final ItemType EDITOR_CUSTOM_MODEL_DATA = new ItemType(50, "&#87F5A0&lEdit Custom Model Data", Material.PAPER, modLore);
    public static final ItemType EDITOR_COLOR = new ItemType(51, "&#87F5A0&lEdit Color", Material.RED_DYE, modLore);
    public static final ItemType EDITOR_DRINK_MESSAGE = new ItemType(52, "&#87F5A0&lEdit Drink Message", Material.WRITABLE_BOOK, modLore);
    public static final ItemType EDITOR_DRINK_TITLE = new ItemType(53, "&#87F5A0&lEdit Drink Title", Material.WRITABLE_BOOK, modLore);





    private String FIELD_NAME;
    private String perm;

    private final Integer slot;
    private final String displayName;
    private final Material m;
    private final List<String> lore;

    public ItemType(@Nullable Integer slot, String displayName, Material m) {
        this.slot = slot;
        this.displayName = displayName;
        this.m = m;
        this.lore = null;
        this.perm = null;
    }

    public ItemType(@Nullable Integer slot, String displayName, Material m, String... lore) {
        this.slot = slot;
        this.displayName = displayName;
        this.m = m;
        this.lore = Arrays.stream(lore).toList();
        this.perm = null;
    }

    public ItemType(@Nullable Integer slot, String displayName, Material m, List<String> lore) {
        this.slot = slot;
        this.displayName = displayName;
        this.m = m;
        this.lore = lore;
        this.perm = null;
    }

    public ItemType() {
        this.slot = null;
        this.displayName = null;
        this.m = null;
        this.lore = null;
        this.perm = null;
    }

    @Nullable
    public Integer getSlot() {
        return slot;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    @Nullable
    public Material getMaterial() {
        return m;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    public String getPerm() {
        return perm;
    }

    @Nullable
    public ItemStack getItem() {
        return getItem(null);
    }

    @Nullable
    public ItemStack getItemForItemStack(ItemStack itemStack) {
        return getItemForItemStack(itemStack, null);
    }

    @Nullable
    public ItemStack getItem(@Nullable String data) {
        if (m == null) {
            return null;
        }
        var itemStack = new ItemStack(m);
        var meta = itemStack.getItemMeta();

        meta.setDisplayName(BUtil.color(displayName));
        if (lore != null) {
            meta.setLore(Util.colorList(lore));
        }
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        itemStack.setItemMeta(meta);
        return getItemForItemStack(itemStack, data);
    }


    @Nullable
    public ItemStack getItemForItemStack(@Nullable ItemStack itemStack, String data) {
        if (itemStack == null) {
            return null;
        }
        var meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(BreweryPlugin.getInstance(), "action"), PersistentDataType.STRING, FIELD_NAME);
        if (data != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(BreweryPlugin.getInstance(), "data"), PersistentDataType.STRING, data);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }



    @Nullable
    public static ItemType getItemType(ItemStack itemStack) {
        var data = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BreweryPlugin.getInstance(), "action"), PersistentDataType.STRING);
        if (data == null) {
            return null;
        }
        return ItemType.valueOf(data);
    }


    // Make this class work like an enum

    public String fieldName() {
        return FIELD_NAME;
    }

    public String getName() {
        return FIELD_NAME;
    }

    public static final Map<String, ItemType> VALUES = new HashMap<>();

    static {
        for (Field field : ItemType.class.getDeclaredFields()) {
            if (field.getType() == ItemType.class) {
                try {
                    ItemType itemType = (ItemType) field.get(null);
                    itemType.FIELD_NAME = field.getName();
                    itemType.perm = "brewery.gui." + field.getName().toLowerCase();
                    VALUES.put(field.getName(), itemType);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ItemType valueOf(String name) {
        return VALUES.get(name);
    }

    public static List<ItemType> values() {
        return VALUES.values().stream().toList();
    }

}
