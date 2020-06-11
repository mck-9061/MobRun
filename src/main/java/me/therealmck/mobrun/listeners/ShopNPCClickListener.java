package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Shop;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopNPCClickListener implements Listener {
    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        String name = npc.getName();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
        Utils utils = new Utils();

        for (Shop shop : Main.activeShops) {
            if (shop.getNpcName().equals(name)) {
                if (event.getClicker().hasPermission(shop.getPermission())) {
                    // Create shop GUI and open it for player

                    ConfigurationSection items = shop.getItems();

                    Inventory gui = Bukkit.createInventory(event.getClicker(), 54, shop.getDisplayName());
                    int count = 0;

                    for (String key : items.getKeys(false)) {
                        ItemStack item = items.getItemStack(key+".Item");
                        ItemMeta meta = item.getItemMeta();
                        ItemStack original = item;
                        item = item.clone();

                        if (meta.hasLore()) {
                            List<String> lore = meta.getLore();
                            lore.addAll(shop.getItemLore());
                            List<String> finalLore = new ArrayList<>();

                            for (String s : lore) {
                                String newS = utils.replaceShopPlaceholders(s, shop, Main.getMobrunConfig(), original);
                                finalLore.add(newS);
                            }

                            meta.setLore(finalLore);
                        } else {
                            List<String> lore = shop.getItemLore();
                            List<String> finalLore = new ArrayList<>();

                            for (String s : lore) {
                                String newS = utils.replaceShopPlaceholders(s, shop, Main.getMobrunConfig(), original);
                                finalLore.add(newS);
                            }

                            meta.setLore(finalLore);
                        }

                        item.setItemMeta(meta);
                        gui.setItem(count, item);
                        count++;
                    }

                    event.getClicker().openInventory(gui);
                } else {
                    event.getClicker().sendMessage(lang.getCannotOpenShopGui());
                }
            }
        }
    }
}
