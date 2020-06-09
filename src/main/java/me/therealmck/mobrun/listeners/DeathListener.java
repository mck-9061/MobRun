package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Utils utils = new Utils();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                boolean shouldStop = true;
                boolean foundPlayer = false;
                for (Player player : subRun.getLobby().getPlayers()) {
                    if (player.getUniqueId().equals(event.getEntity().getUniqueId())) {
                        foundPlayer = true;
                        event.setKeepInventory(true);
                        event.setKeepLevel(true);
                        event.getEntity().setHealth(20);
                        event.getEntity().setGameMode(GameMode.SPECTATOR);
                    }
                }
                if (!foundPlayer) return;

                for (Player player : subRun.getLobby().getPlayers()) {
                    if (!player.getGameMode().equals(GameMode.SPECTATOR)) shouldStop = false;
                }

                if (shouldStop) {
                    // Get location to TP back to
                    for (NPC npc : CitizensAPI.getNPCRegistry()) {
                        if (npc.getName().equals(subRun.getRun().getNpcName())) {
                            for (Player p : subRun.getLobby().getPlayers()) {
                                // Teleporting on main thread so bukkit doesn't shout at me
                                Bukkit.getScheduler().runTask(Main.instance, () -> {
                                    p.setGameMode(GameMode.SURVIVAL);
                                    p.teleport(npc.getStoredLocation());
                                });
                                p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getAllPlayersDead(), subRun.getRun(), subRun.getLobby()));
                            }
                        }
                    }
                    subRun.getLobby().stopRunning();
                }
            }
        }
    }
}
