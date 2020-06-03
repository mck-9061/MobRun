package me.therealmck.mobrun.utils;

import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.Shop;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Utils {
    public Utils() {}

    public String replaceRunPlaceholders(String string, Run run, FileConfiguration config) {
        String toReplace = string;
        toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
        toReplace = toReplace.replace(config.getString("RunPointsNamePlaceholder"), run.getPointsName());
        toReplace = toReplace.replace(config.getString("RunPointsAmountPlaceholder"), String.valueOf(run.getPointsReward()));
        toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());

        return toReplace;
    }

    public String replaceLobbyPlaceholders(String string, Lobby lobby, FileConfiguration config) {
        String toReplace = string;


        return toReplace;
    }

    public String replaceShopPlaceholders(String string, Shop shop, FileConfiguration config) {
        String toReplace = string;


        return toReplace;
    }

    public ItemStack newItemWithNameAndLore(Material material, int amount, String name, List<String> lore, short colour) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
