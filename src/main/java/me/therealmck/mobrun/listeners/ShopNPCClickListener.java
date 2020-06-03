package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Shop;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

                    HashMap<ItemStack, Integer> items = shop.getItems();
                    int rowsNeeded = ((items.keySet().size() % 9) + 1) * 9;

                    Inventory gui = Bukkit.createInventory(event.getClicker(), rowsNeeded, shop.getDisplayName());
                    int count = 0;

                    for (ItemStack item : items.keySet()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.hasLore()) {
                            List<String> lore = meta.getLore();
                            // TODO: Placeholders
                            lore.addAll(shop.getItemLore());
                            meta.setLore(lore);
                        } else {
                            List<String> lore = shop.getItemLore();
                            meta.setLore(lore);
                        }
                        item = item.clone();
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
