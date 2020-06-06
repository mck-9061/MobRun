package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                if (subRun.getLobby().getPlayers().contains(p) && subRun.getLobby().isRunning()) {
                    if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) {
                        event.setCancelled(true);
                        Utils utils = new Utils();
                        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
                        p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getCannotLeave(), run, subRun.getLobby()));
                    }
                }
            }
        }
    }
}
