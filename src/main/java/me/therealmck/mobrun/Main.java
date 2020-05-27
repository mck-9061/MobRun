package me.therealmck.mobrun;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    public static File mobrunFile;
    public static FileConfiguration mobrunConfig;

    @Override
    public void onEnable() {
        createMobrunConfig();

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
