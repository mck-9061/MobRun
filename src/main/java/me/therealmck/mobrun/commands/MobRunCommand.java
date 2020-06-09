package me.therealmck.mobrun.commands;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.Shop;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MobRunCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("Not enough arguments provided.");
            return false;
        } else {
            MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
            Utils utils = new Utils();

            if (args[0].equals("shop")) {
                if (commandSender.hasPermission("mobrun.admin")) {
                    try {
                        if (args[1].equals("create")) {
                            for (Shop shop : Main.activeShops) {
                                if (shop.getId().equals(args[2])) {
                                    commandSender.sendMessage(lang.getCreateShopAlreadyExists());
                                    return true;
                                }
                            }
                            ConfigurationSection section = Main.getMobrunConfig().createSection("Shops."+args[2]);
                            section.set("HookThisRun", "Not set");
                            section.set("DisplayName", "Not set");
                            section.set("NPCname", "Not set");
                            section.set("ShopGUIopenPermission", "Not set");
                            section.set("PriceLore", Arrays.asList("§r§c---------------------", "§r§c{price} {runpointsname}s"));
                            section.createSection("Items");
                            Main.saveMobrunConfig();

                            commandSender.sendMessage(lang.getCreateShopSuccess());

                        } else if (args[1].equals("add")) {
                            if (commandSender instanceof Player) {
                                ItemStack item = ((Player) commandSender).getInventory().getItemInMainHand();
                                if (item == null) {
                                    commandSender.sendMessage("You're not holding any item.");
                                } else {
                                    Shop shop = null;
                                    for (Shop a : Main.activeShops) {
                                        if (a.getId().equals(args[2])) {
                                            shop = a;
                                        }
                                    }
                                    if (shop == null) {
                                        commandSender.sendMessage("That shop doesn't exist.");
                                    } else {
                                        shop.addItem(item, Integer.valueOf(args[3]));
                                        commandSender.sendMessage(utils.replaceShopPlaceholders(lang.getAddItemToShopSuccess(), shop, Main.getMobrunConfig(), item));
                                    }
                                }
                            } else {
                                commandSender.sendMessage("This command can only be ran as a player.");
                            }
                        } else {
                            commandSender.sendMessage("Invalid command. Use like this: /mobrun shop [create/add] [shop name] [price (if using /mobrun shop add)]");
                        }
                    } catch (Exception e) {
                        commandSender.sendMessage("Invalid command. Use like this: /mobrun shop [create/add] [shop name] [price (if using /mobrun shop add)]");
                        e.printStackTrace();
                    }
                } else {
                    commandSender.sendMessage("You don't have permission to use this command!");
                }
            } else if (args[0].equals("info")) {
                if (commandSender instanceof Player) {
                    commandSender.sendMessage(lang.getDisplayRunPoints());
                    for (Run run : Main.activeRuns) {
                        int points = Main.getPlayerConfig().getInt(((Player) commandSender).getUniqueId().toString()+"."+run.getPointsName());
                        commandSender.sendMessage(run.getPointsName()+": "+ points);
                    }
                } else {
                    commandSender.sendMessage("Only a player can use this command.");
                }
            } else {
                commandSender.sendMessage("Invalid command.");
            }
        }


        return true;
    }
}
