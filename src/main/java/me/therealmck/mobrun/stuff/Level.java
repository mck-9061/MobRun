package me.therealmck.mobrun.stuff;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private String id;
    private SubRun subRun;

    private Location checkpoint;
    private String npcName;
    private String keyItem;
    private String serverCommand;
    private Location initialTeleport;
    private Boolean isFinalLevel;

    public Level(String id, SubRun subRun, FileConfiguration config) {
        this.id = id;
        this.subRun = subRun;

        this.npcName = config.getString(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".NPCname");
        this.keyItem = config.getString(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".KeyItemDisplayName");
        this.serverCommand = config.getString(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".ServerCommand");
        this.isFinalLevel = config.getBoolean(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".FinalLevel");

        String[] xyz = config.getString(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".CheckPointLocation").split(" ");
        this.checkpoint = new Location(Bukkit.getWorld(config.getString(subRun.getRun().getId()+
                ".RunProcessInformation."+subRun.getId()+"."+id+".WorldNameCheckPoint")), Integer.parseInt(xyz[0]),
                Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));

        String[] xyz2 = config.getString(subRun.getRun().getId()+".RunProcessInformation."+subRun.getId()+"."+id+".TeleportToLocation").split(" ");
        this.initialTeleport = new Location(Bukkit.getWorld(config.getString(subRun.getRun().getId()+
                ".RunProcessInformation."+subRun.getId()+"."+id+".WorldNameTeleport")), Integer.parseInt(xyz2[0]),
                Integer.parseInt(xyz2[1]), Integer.parseInt(xyz2[2]));
    }

    public String getId() {
        return id;
    }

    public SubRun getSubRun() {
        return subRun;
    }

    public Location getCheckpoint() {
        return checkpoint;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getKeyItem() {
        return keyItem;
    }

    public Location getInitialTeleport() {
        return initialTeleport;
    }

    public Boolean getFinalLevel() {
        return isFinalLevel;
    }

    public String getServerCommand() { return serverCommand; }
}
