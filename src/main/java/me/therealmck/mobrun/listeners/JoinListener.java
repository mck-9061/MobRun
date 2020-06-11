package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

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

        // Send messages if the server just shut down
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String key : Main.getDataConfig().getKeys(false)) {
                    MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
                    if (key.equals(event.getPlayer().getUniqueId().toString())) {
                        Player p = event.getPlayer();
                        for (NPC npc : CitizensAPI.getNPCRegistry()) {
                            if (npc.getName().equals(Main.getDataConfig().get(key + ".npc"))) {
                                p.teleport(npc.getEntity().getLocation());
                                p.sendMessage(lang.getAfterShutDown1());
                                p.sendMessage(lang.getAfterShutDown2());
                            }
                        }
                        Main.getDataConfig().set(key, null);
                        Main.saveDataConfig();
                    }
                }
            }
        }.runTaskLater(Main.instance, 10L);


        // TP Player to last checkpoint if they're in a lobby
        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                if (!subRun.getLobby().isRunning()) continue;
                boolean shouldSwap = false;
                Player swapFrom = null;
                for (Player player : subRun.getLobby().getPlayers()) {
                    if (player.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                        shouldSwap = true;
                        swapFrom = player;
                        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
                        event.getPlayer().sendMessage(lang.getBackToGameAfterDisconnect());
                        event.getPlayer().teleport(subRun.getLobby().getCurrentLevel().getCheckpoint());
                    }
                }

                if (shouldSwap) {
                    subRun.getLobby().swapPlayer(swapFrom, event.getPlayer());
                    subRun.getLobby().fixBossBar();
                }
            }
        }

        // TP Player to NPC if they defected
        for (UUID uuid : Main.defectors.keySet()) {
            if (uuid.equals(event.getPlayer().getUniqueId())) {
                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {event.getPlayer().teleport(Main.defectors.get(uuid));}, 10L);
                Main.defectors.remove(uuid);
            }
        }

    }
}
