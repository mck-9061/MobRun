package me.therealmck.mobrun;

import gnu.trove.map.hash.THashMap;
import me.therealmck.mobrun.commands.MobRunCommand;
import me.therealmck.mobrun.listeners.*;
import me.therealmck.mobrun.listeners.inventory.RunInventoryClickListener;
import me.therealmck.mobrun.listeners.inventory.ShopInventoryClickListener;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.Shop;
import me.therealmck.mobrun.stuff.SubRun;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin {
    public static File mobrunFile;
    public static FileConfiguration mobrunConfig;
    public static File playerFile;
    public static FileConfiguration playerConfig;
    public static File dataFile;
    public static FileConfiguration dataConfig;
    public static File shopsFile;
    public static FileConfiguration shopsConfig;
    public static Plugin instance;
    public static HashMap<UUID, Location> defectors = new HashMap<>();

    // These fields just store all active stuff, hence why it's a terrible idea to hot-reload
    public static List<Run> activeRuns = new ArrayList<>();
    public static List<Shop> activeShops = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        createMobrunConfig();
        createPlayerConfig();
        createDataConfig();
        createShopsConfig();
        saveResource("configtemplate.yml", true);

        // Add all runs/shops etc. from config

        // This is kinda hacky but I cba moving everything to a better system
        List<String> sysKeys = Arrays.asList("MembersJoinedPlaceholder", "ActiveMembersNamesPlaceholder", "LobbyIDPlaceholder", "RunNamePlaceholder",
                "RunPointsNamePlaceholder", "RunPointsAmountPlaceholder", "ItemPricePlaceholder", "LeftOverMinutesPlaceholder", "ShopNamePlaceholder",
                "Shops", "CanNotOpenRunGUImsg", "CanNotOpenShopGUImsg", "RunStartMsg", "PassedLevelMsg", "RunFinishMsg", "ShopNotEnoughPointsMsg",
                "ShopBoughtSuccessfullyMsg", "DisplayRunPointsMsg", "CouldNotFinishInTimeMsg", "BackInGameMsg", "AfterShutDown1Msg", "AfterShutDown2Msg",
                "CanNotLeaveMsg", "MobrunShopCreateCommandSuccessMsg", "MobrunShopCreateCommandFailureMsg", "MobrunShopAddCommandSuccessMsg",
                "MobrunShopAddCommandFailureMsg", "LobbyFullMsg", "AllPlayersDeadMsg", "AmountOfPlayers", "LobbyJoinMsg", "LobbyLeaveMsg", "TimeoutMsg",
                "AlreadyInLobbyMsg");

        for (String key : mobrunConfig.getKeys(false)) {
            if (!sysKeys.contains(key)) activeRuns.add(new Run(mobrunConfig, key));
        }

        // Shops
        for (String key : shopsConfig.getKeys(false)) {
            activeShops.add(new Shop(key));
        }

        // Register all the listeners
        getServer().getPluginManager().registerEvents(new RunInventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new KeyNPCClickListener(), this);
        getServer().getPluginManager().registerEvents(new RunNPCClickListener(), this);
        getServer().getPluginManager().registerEvents(new ShopNPCClickListener(), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new ShopInventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        // Register the command
        this.getCommand("mobrun").setExecutor(new MobRunCommand());
    }

    @Override
    public void onDisable() {
        // Save players that should be notified

        for (Run run : activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                for (Player p : subRun.getLobby().getPlayers()) {
                    for (NPC npc : CitizensAPI.getNPCRegistry()) {
                        if (npc.getName().equals(run.getNpcName())) {
                            dataConfig.set(p.getUniqueId()+".npc", npc.getName());
                            saveDataConfig();
                        }
                    }
                }
            }
        }
    }

    public static FileConfiguration getMobrunConfig() {
        return mobrunConfig;
    }

    private void createMobrunConfig() {
        mobrunFile = new File(getDataFolder(), "config.yml");
        if (!mobrunFile.exists()) {
            mobrunFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        mobrunConfig = new YamlConfiguration();
        try {
            mobrunConfig.load(mobrunFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveMobrunConfig() {
        try {
            mobrunConfig.save(mobrunFile);
        } catch (Exception e) {e.printStackTrace();}
    }


    public static FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

    private void createPlayerConfig() {
        playerFile = new File(getDataFolder(), "playerdata.yml");
        if (!playerFile.exists()) {
            playerFile.getParentFile().mkdirs();
            saveResource("playerdata.yml", false);
        }

        playerConfig = new YamlConfiguration();
        try {
            playerConfig.load(playerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerConfig() {
        try {
            playerConfig.save(playerFile);
        } catch (Exception e) {e.printStackTrace();}
    }


    public static FileConfiguration getDataConfig() {
        return dataConfig;
    }

    private void createDataConfig() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        dataConfig = new YamlConfiguration();
        try {
            dataConfig.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (Exception e) {e.printStackTrace();}
    }


    public static FileConfiguration getShopsConfig() {
        return shopsConfig;
    }

    private void createShopsConfig() {
        shopsFile = new File(getDataFolder(), "shops.yml");
        if (!shopsFile.exists()) {
            shopsFile.getParentFile().mkdirs();
            saveResource("shops.yml", false);
        }

        shopsConfig = new YamlConfiguration();
        try {
            shopsConfig.load(shopsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveShopsConfig() {
        try {
            shopsConfig.save(shopsFile);
        } catch (Exception e) {e.printStackTrace();}
    }
}
