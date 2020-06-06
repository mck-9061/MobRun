package me.therealmck.mobrun.stuff;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SubRun {
    private String id;
    private Run run;
    private List<Level> levels;
    private Lobby lobby;

    public SubRun(String id, Run run, FileConfiguration config) {
        this.id = id;
        this.run = run;
        this.levels = new ArrayList<>();

        ConfigurationSection section = config.getConfigurationSection(run.getId()+".RunProcessInformation."+id);

        for (String key : section.getKeys(false)) {
            levels.add(new Level(key, this, config));
        }

        this.lobby = new Lobby(this);
    }

    public String getId() {
        return id;
    }

    public Run getRun() {
        return run;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public Lobby getLobby() { return lobby; }
}
