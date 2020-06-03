package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
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



        // TP Player to last checkpoint if they're in a lobby

        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                if (subRun.getLobby().getPlayers().contains(event.getPlayer())) {
                    MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
                    event.getPlayer().sendMessage(lang.getBackToGameAfterDisconnect());
                    event.getPlayer().teleport(subRun.getLobby().getCurrentLevel().getCheckpoint());
                    // TODO: Bossbar
                }
            }
        }
    }
}
