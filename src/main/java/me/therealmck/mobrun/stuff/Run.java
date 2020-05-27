package me.therealmck.mobrun.stuff;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Run {
    private String id;
    private String name;
    private String pointsName;
    private int pointsReward;
    private String npcName;
    private String openPermission;
    private String availableWoolName;
    private String closedWoolName;
    private List<String> woolLore;
    private int minutes;
    private String bossBarTitle;
    private List<SubRun> availableRuns;

    public Run(FileConfiguration config, String id) {
        this.id = id;

        this.availableRuns = new ArrayList<>();

        this.name = config.getString(id+".name");
        this.pointsName = config.getString(id+".ThisRunPointsName");
        this.pointsReward = config.getInt(id+".ThisRunPointsAmount");
        this.npcName = config.getString(id+".ThisRunNPCstartName");
        this.openPermission = config.getString(id+".PlayGUIopenPermission");
        this.availableWoolName = config.getString(id+".WoolNameWhenCanJoin");
        this.closedWoolName = config.getString(id+".WoolNameWhenCanNotJoin");
        this.woolLore = config.getStringList(id+".WoolLore");
        this.minutes = config.getInt(id+".PlayTimeInMinutes");
        this.bossBarTitle = config.getString(id+".BossBarTitle");

        ConfigurationSection subRunSection = config.getConfigurationSection(id+".RunProcessInformation");

        for (String subRunKey : subRunSection.getKeys(false)) {
            availableRuns.add(new SubRun(subRunKey, this, config));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPointsName() {
        return pointsName;
    }

    public int getPointsReward() {
        return pointsReward;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getOpenPermission() {
        return openPermission;
    }

    public String getAvailableWoolName() {
        return availableWoolName;
    }

    public String getClosedWoolName() {
        return closedWoolName;
    }

    public List<String> getWoolLore() {
        return woolLore;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getBossBarTitle() {
        return bossBarTitle;
    }

    public List<SubRun> getAvailableRuns() {
        return availableRuns;
    }
}
