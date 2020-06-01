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
    private List<String> itemLore;
    private HashMap<ItemStack, Integer> items;

    public Shop(String shopName) {
        FileConfiguration config = Main.getMobrunConfig();
        ConfigurationSection section = config.getConfigurationSection("Shops."+shopName);

        this.runHook = new Run(config, section.getString("HookThisRun"));
        this.displayName = section.getString("DisplayName");
        this.npcName = section.getString("NPCname");
        this.permission = section.getString("ShopGUIopenPermission");
        this.itemLore = section.getStringList("PriceLore");
        this.items = (HashMap<ItemStack, Integer>) section.get("Items");
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

    public HashMap<ItemStack, Integer> getItems() {
        return items;
    }

    public void addItem(ItemStack item, Integer price) {
        items.put(item, price);
        Main.getMobrunConfig().set("Shops."+displayName+".Items", items);
        Main.saveMobrunConfig();
    }
}
