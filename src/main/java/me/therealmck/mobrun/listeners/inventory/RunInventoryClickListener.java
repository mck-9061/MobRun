package me.therealmck.mobrun.listeners.inventory;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RunInventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String invName = event.getView().getTitle();
        Player p = (Player) event.getWhoClicked();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
        Utils utils = new Utils();

        for (Run run : Main.activeRuns) {
            if (run.getName().equals(invName)) {
                try {
                    p.closeInventory();
                    Lobby lobby = run.getAvailableRuns().get(event.getRawSlot()).getLobby();

                    if (lobby.isRunning()) {
                        p.sendMessage(lang.getLobbyFull());
                        return;
                    }

                    lobby.addPlayer(p);

                    if (lobby.getPlayers().size() == 5) {
                        // Begin run
                        lobby.startRunning();

                        // Start run 10 seconds later
                        for (Player player : lobby.getPlayers()) player.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getRunStart(), run, lobby));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // Start timer
                                BukkitRunnable timer = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        lobby.setSecondsLeft(lobby.getSecondsLeft()-1);

                                        // TODO: Bossbar

                                        if (lobby.getSecondsLeft() == 0) {

                                            // Get location to TP back to
                                            for (NPC npc : CitizensAPI.getNPCRegistry()) {
                                                if (npc.getName().equals(run.getNpcName())) {
                                                    for (Player p : lobby.getPlayers()) {
                                                        // Teleporting on main thread so bukkit doesn't shout at me
                                                        Bukkit.getScheduler().runTask(Main.instance, () -> {p.teleport(npc.getStoredLocation());});
                                                        p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getDidNotFinishInTime(), run, lobby));
                                                    }
                                                }
                                            }

                                            lobby.stopRunning();
                                            this.cancel();
                                        }
                                    }
                                };
                                timer.runTaskTimerAsynchronously(Main.instance, 0L, 20L);

                                // Start run
                                for (Player player : lobby.getPlayers()) {
                                    player.teleport(lobby.getCurrentLevel().getInitialTeleport());
                                }



                            }
                        }.runTaskLater(Main.instance, 200L);
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
