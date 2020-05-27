package me.therealmck.mobrun.utils;

import me.therealmck.mobrun.stuff.Run;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {
    public String replaceRunPlaceholders(String string, Run run, FileConfiguration config) {
        String toReplace = string;
        toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());
        toReplace = toReplace.replace(config.getString("RunPointsNamePlaceholder"), run.getPointsName());
        toReplace = toReplace.replace(config.getString("RunPointsAmountPlaceholder"), String.valueOf(run.getPointsReward()));
        toReplace = toReplace.replace(config.getString("RunNamePlaceholder"), run.getName());

        return toReplace;
    }

    public String replaceLobbyPlaceholders(String string, Run run, FileConfiguration config) {
        String toReplace = string;


        return toReplace;
    }

    public String replaceShopPlaceholders(String string, Run run, FileConfiguration config) {
        String toReplace = string;


        return toReplace;
    }
}
