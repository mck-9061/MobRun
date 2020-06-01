package me.therealmck.mobrun;

import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.Shop;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {
    public static File mobrunFile;
    public static FileConfiguration mobrunConfig;

    // These fields just store all active stuff, hence why it's a terrible idea to hot-reload
    public static List<Run> activeRuns;
    public static List<Lobby> activeLobbies;
    public static List<Shop> activeShops;

    @Override
    public void onEnable() {
        createMobrunConfig();

        // Add all runs/shops/lobbies etc. from config

        // This is kinda hacky but I cba moving everything to a better system
        List<String> sysKeys = Arrays.asList("", "");
    }

    @Override
    public void onDisable() {

    }

    public static FileConfiguration getMobrunConfig() {
        return mobrunConfig;
    }

    private void createMobrunConfig() {
        mobrunFile = new File(getDataFolder(), "slots.yml");
        if (!mobrunFile.exists()) {
            mobrunFile.getParentFile().mkdirs();
            saveResource("slots.yml", false);
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
}
