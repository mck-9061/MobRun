package me.therealmck.mobrun.listeners.inventory;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Shop;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopInventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String invName = event.getView().getTitle();
        Player p = (Player) event.getWhoClicked();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
        Utils utils = new Utils();

        for (Shop shop : Main.activeShops) {
            if (invName.equals(shop.getDisplayName())) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getType() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

                int index = event.getRawSlot()+1;
                ItemStack toGive = shop.getItems().getItemStack(index+".Item");
                if (toGive == null) return;
                int price = shop.getItemPrice(toGive);
                int balance = Main.getPlayerConfig().getInt(event.getWhoClicked().getUniqueId()+"."+shop.getRunHook().getPointsName());

                if (price <= balance) {
                    balance -= price;
                    Main.getPlayerConfig().set(event.getWhoClicked().getUniqueId()+"."+shop.getRunHook().getPointsName(), balance);
                    Main.savePlayerConfig();
                    event.getWhoClicked().getInventory().addItem(toGive);
                    event.getWhoClicked().sendMessage(utils.replaceShopPlaceholders(lang.getBoughtSuccessfully(), shop, Main.getMobrunConfig(), toGive));
                } else {
                    event.getWhoClicked().sendMessage(utils.replaceShopPlaceholders(lang.getNotEnoughPoints(), shop, Main.getMobrunConfig(), toGive));
                }
            }
        }
    }
}
