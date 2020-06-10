package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Level;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class KeyNPCClickListener implements Listener {
    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        for (Run run : Main.activeRuns) {
            for (SubRun subRun : run.getAvailableRuns()) {
                Player p = event.getClicker();
                if (subRun.getLobby().getPlayers().contains(p)) {
                    Lobby lobby = subRun.getLobby();
                    Level currentLevel = lobby.getCurrentLevel();
                    ItemStack heldItem = p.getInventory().getItemInMainHand();

                    //Check it's the correct NPC
                    if (!event.getNPC().getName().equals(lobby.getCurrentLevel().getNpcName())) {
                        return;
                    }

                    try {
                        if (heldItem.getItemMeta().hasDisplayName()) {
                            // Strip format codes for comparison
                            String heldItemName = heldItem.getItemMeta().getDisplayName();
                            String keyName = currentLevel.getKeyItem();

                            StringBuilder compareHeldItemName = new StringBuilder(heldItemName);
                            StringBuilder compareKeyName = new StringBuilder(keyName);
                            int count = 0;

                            for (Character c : heldItemName.toCharArray()) {
                                if (c.equals('§') || c.equals('&')) {
                                    try {
                                        compareHeldItemName.deleteCharAt(count);
                                        compareHeldItemName.deleteCharAt(count + 1);
                                    } catch (Exception ignored) {
                                    }
                                }
                                count++;
                            }

                            count = 0;

                            for (Character c : keyName.toCharArray()) {
                                if (c.equals('§') || c.equals('&')) {
                                    try {
                                        compareKeyName.deleteCharAt(count);
                                        compareKeyName.deleteCharAt(count + 1);
                                    } catch (Exception ignored) {
                                    }
                                }
                                count++;
                            }


                            if (compareHeldItemName.toString().equals(compareKeyName.toString())) {
                                MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
                                Utils utils = new Utils();
                                p.getInventory().remove(heldItem);
                                if (currentLevel.getFinalLevel()) {
                                    // Get location to TP back to
                                    for (NPC npc : CitizensAPI.getNPCRegistry()) {
                                        if (npc.getName().equals(run.getNpcName())) {
                                            for (Player pl : lobby.getPlayers()) {
                                                // Teleporting on main thread so bukkit doesn't shout at me
                                                Bukkit.getScheduler().runTask(Main.instance, () -> {
                                                    pl.teleport(npc.getStoredLocation());
                                                });
                                                pl.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getRunFinish(), run, lobby));

                                                // Add run points
                                                if (pl.isOnline()) {
                                                    int currentPoints = Main.getPlayerConfig().getInt(pl.getUniqueId() + "." + run.getPointsName());
                                                    currentPoints += run.getPointsReward();
                                                    Main.getPlayerConfig().set(pl.getUniqueId() + "." + run.getPointsName(), currentPoints);
                                                    Main.savePlayerConfig();
                                                } else {
                                                    Main.defectors.put(pl, npc.getStoredLocation());
                                                }
                                            }
                                        }
                                    }

                                    lobby.stopRunning();
                                } else {
                                    lobby.increaseCurrentLevel();
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lobby.getCurrentLevel().getServerCommand());
                                    for (Player player : lobby.getPlayers()) {
                                        player.teleport(lobby.getCurrentLevel().getCheckpoint());
                                        player.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getPassedLevel(), run, lobby));
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
