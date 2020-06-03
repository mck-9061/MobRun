package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!Main.playerConfig.getKeys(false).contains(event.getPlayer().getUniqueId().toString())) {
            for (Run run : Main.activeRuns) {
                Main.playerConfig.set(event.getPlayer().getUniqueId().toString()+"."+run.getPointsName(), 0);
            }
        }

        ConfigurationSection section = Main.playerConfig.getConfigurationSection(event.getPlayer().getUniqueId().toString());
        for (Run run : Main.activeRuns) {
            if (!section.getKeys(false).contains(run.getPointsName())) Main.playerConfig.set(event.getPlayer().getUniqueId().toString()+"."+run.getPointsName(), 0);
        }

        Main.savePlayerConfig();
    }
}
