package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                if (subRun.getLobby().getPlayers().contains(event.getPlayer()) && !subRun.getLobby().isRunning()) {
                    subRun.getLobby().removePlayer(event.getPlayer());
                }
            }
        }
    }
}
