package me.therealmck.mobrun.listeners.inventory;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
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

                    for (Run r : Main.activeRuns) {
                        for (SubRun s : r.getAvailableRuns()) {
                            if (s.getLobby().getPlayers().contains(p)) {
                                p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getAlreadyInLobby(), r, s.getLobby()));
                                return;
                            }
                        }
                    }

                    lobby.addPlayer(p);
                    p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getJoinedLobby(), run, lobby));

                    // Schedule timeout
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!lobby.isRunning()) {
                                lobby.removePlayer(p);
                                p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getTimeout(), run, lobby));
                            }
                        }
                    }.runTaskLater(Main.instance, 12000L);

                    if (lobby.getPlayers().size() == Main.getMobrunConfig().getInt("AmountOfPlayers")) {
                        lobby.setRunning(true);

                        // Start run 10 seconds later
                        for (Player player : lobby.getPlayers()) player.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getRunStart(), run, lobby));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // Start run
                                lobby.startRunning();
                                for (Player player : lobby.getPlayers()) {
                                    player.teleport(lobby.getCurrentLevel().getCheckpoint());
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lobby.getCurrentLevel().getServerCommand());
                            }
                        }.runTaskLater(Main.instance, 200L);
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
