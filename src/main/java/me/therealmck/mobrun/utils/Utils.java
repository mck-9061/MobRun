package me.therealmck.mobrun.utils;

import jdk.internal.jline.internal.Nullable;
import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.Shop;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Utils {
    public Utils() {}

    public String replaceRunPlaceholders(String string, Run run, FileConfiguration config) {
        String toReplace = string;
        try {
            toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
            toReplace = toReplace.replace(config.getString("RunPointsNamePlaceholder"), run.getPointsName());
            toReplace = toReplace.replace(config.getString("RunPointsAmountPlaceholder"), String.valueOf(run.getPointsReward()));
            toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
        } catch (Exception ignored) {}

        return toReplace;
    }

    public String replaceLobbyPlaceholders(String string, Lobby lobby, FileConfiguration config) {
        String toReplace = string;
        toReplace = toReplace.replace(config.getString("MembersJoinedPlaceholder"), String.valueOf(lobby.getPlayers().size()));

        // Players in a lobby
        List<Player> players = lobby.getPlayers();
        String finalString = "";

        for (Player player : players) {
            finalString += player.getName() + ", ";
        }

        try {
            finalString = finalString.substring(0, finalString.length() - 2);
        } catch (Exception ignored) {}

        toReplace = toReplace.replace(config.getString("ActiveMembersNamesPlaceholder"), finalString);
        toReplace = toReplace.replace(config.getString("LobbyIDPlaceholder"), lobby.getSubRun().getId());
        toReplace = toReplace.replace(config.getString("LeftOverMinutesPlaceholder"), String.valueOf((lobby.getSecondsLeft()/60)+1));

        return toReplace;
    }

    public String replaceShopPlaceholders(String string, Shop shop, FileConfiguration config, ItemStack originalItem) {
        String toReplace = string;

        if (shop.getItemPrice(originalItem) == -1) {
            toReplace = toReplace.replace(config.getString("ItemPricePlaceholder"), "Invalid price!");
        } else {
            toReplace = toReplace.replace(config.getString("ItemPricePlaceholder"), String.valueOf(shop.getItemPrice(originalItem)));
        }
        toReplace = toReplace.replace(config.getString("ShopNamePlaceholder"), shop.getDisplayName());

        Run run = shop.getRunHook();
        try {
            toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
            toReplace = toReplace.replace(config.getString("RunPointsNamePlaceholder"), run.getPointsName());
            toReplace = toReplace.replace(config.getString("RunPointsAmountPlaceholder"), String.valueOf(run.getPointsReward()));
            toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
        } catch (Exception ignored) {}

        return toReplace;
    }

    public String replaceRunAndLobbyPlaceholders(String string, Run run, Lobby lobby) {
        String s = string;
        try {
            s = replaceRunPlaceholders(s, run, Main.getMobrunConfig());
            s = replaceLobbyPlaceholders(s, lobby, Main.getMobrunConfig());
        } catch (Exception e) {

        }
        return s;
    }

    public ItemStack newItemWithNameAndLore(Material material, int amount, String name, List<String> lore, short colour) {
        ItemStack item = new ItemStack(material, amount, colour);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newItemWithNameAndLoreNoColour(Material material, int amount, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
