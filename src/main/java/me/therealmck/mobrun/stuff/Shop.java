package me.therealmck.mobrun.stuff;

import me.therealmck.mobrun.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class Shop {
    private final Run runHook;
    private String displayName;
    private String npcName;
    private String permission;
    private String id;
    private List<String> itemLore;
    private ConfigurationSection items;

    public Shop(String shopName) {
        FileConfiguration config = Main.getMobrunConfig();
        ConfigurationSection section = config.getConfigurationSection("Shops."+shopName);

        this.id = shopName;
        this.runHook = new Run(config, section.getString("HookThisRun"));
        this.displayName = section.getString("DisplayName");
        this.npcName = section.getString("NPCname");
        this.permission = section.getString("ShopGUIopenPermission");
        this.itemLore = section.getStringList("PriceLore");
        this.items = section.getConfigurationSection("Items");
        if (items == null) {
            section.createSection("Items");
            this.items = section.getConfigurationSection("Items");
        }
    }

    public Run getRunHook() {
        return runHook;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getItemLore() {
        return itemLore;
    }

    public ConfigurationSection getItems() {
        return items;
    }

    public void addItem(ItemStack item, Integer price) {
        int count = 0;
        for (String ignored : items.getKeys(false)) count++;

        ConfigurationSection section = items.createSection(String.valueOf(count+1));

        section.set("Item", item);
        section.set("Price", price);

        Main.saveMobrunConfig();
    }

    public String getId() { return id; }

    public int getItemPrice(ItemStack item) {
        for (String key : items.getKeys(false)) {
            if (items.getItemStack(key+".Item").equals(item)) {
                return items.getInt(key+".Price");
            }
        }
        return -1;
    }
}
