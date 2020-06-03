package me.therealmck.mobrun.listeners.inventory;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.utils.MessageHelper;
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
                        for (Player player : lobby.getPlayers()) player.sendMessage(lang.getRunStart());
                        new BukkitRunnable() {
                            @Override
                            public void run() {

                            }
                        }.runTaskLater(Main.instance, 200L);
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
