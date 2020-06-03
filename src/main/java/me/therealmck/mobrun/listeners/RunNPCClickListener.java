package me.therealmck.mobrun.listeners;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.stuff.Lobby;
import me.therealmck.mobrun.stuff.Run;
import me.therealmck.mobrun.stuff.SubRun;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class RunNPCClickListener implements Listener {
    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        String name = npc.getName();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());
        Utils utils = new Utils();

        for (Run run : Main.activeRuns) {
            if (name.equals(run.getNpcName())) {
                if (event.getClicker().hasPermission(run.getOpenPermission())) {
                    List<SubRun> subRuns = run.getAvailableRuns();
                    int rowsNeeded = ((subRuns.size() % 9) + 1) * 9;

                    Inventory gui = Bukkit.createInventory(event.getClicker(), rowsNeeded, run.getName());

                    int count = 0;

                    for (SubRun subRun : subRuns) {
                        if (subRun.getLobby().isRunning()) {
                            gui.setItem(count, utils.newItemWithNameAndLore(Material.CONCRETE, 1, run.getClosedWoolName(), run.getWoolLore(), (short) 14));
                        } else {
                            gui.setItem(count, utils.newItemWithNameAndLore(Material.CONCRETE, 1, run.getAvailableWoolName(), run.getWoolLore(), (short) 13));
                        }
                        count++;
                    }

                    event.getClicker().openInventory(gui);
                } else {
                    event.getClicker().sendMessage(lang.getCannotOpenRunGui());
                }
            }
        }
    }
}
