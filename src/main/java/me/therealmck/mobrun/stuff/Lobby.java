package me.therealmck.mobrun.stuff;

import me.therealmck.mobrun.Main;
import me.therealmck.mobrun.utils.MessageHelper;
import me.therealmck.mobrun.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<Player> players;
    private final SubRun subRun;
    private Level currentLevel;
    private int currentLevelIndex;
    private boolean isRunning;
    private int secondsLeft;
    private BukkitRunnable timer;
    private BossBar bossBar;
    private int maxSeconds;

    public Lobby(SubRun subRun) {
        this.subRun = subRun;
        this.isRunning = false;
        this.secondsLeft = 0;
        this.currentLevel = subRun.getLevels().get(0);
        this.currentLevelIndex = 0;
        this.maxSeconds = subRun.getRun().getMinutes()*60;
        this.players = new ArrayList<>();
        this.bossBar = Bukkit.createBossBar("default", BarColor.GREEN, BarStyle.SOLID);
        Utils utils = new Utils();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());

        this.timer = new BukkitRunnable() {
            @Override
            public void run() {
                setSecondsLeft(getSecondsLeft()-1);

                bossBar.setProgress((double)secondsLeft / (double)maxSeconds);
                bossBar.setTitle(utils.replaceRunAndLobbyPlaceholders(subRun.getRun().getBossBarTitle(), subRun.getRun(), subRun.getLobby()));

                if (bossBar.getProgress() > 0.66) {
                    bossBar.setColor(BarColor.GREEN);
                } else if (bossBar.getProgress() > 0.33) {
                    bossBar.setColor(BarColor.YELLOW);
                } else {
                    bossBar.setColor(BarColor.RED);
                }

                if (getSecondsLeft() == 0) {

                    // Get location to TP back to
                    for (NPC npc : CitizensAPI.getNPCRegistry()) {
                        if (npc.getName().equals(subRun.getRun().getNpcName())) {
                            for (Player p : getPlayers()) {
                                // Teleporting on main thread so bukkit doesn't shout at me
                                Bukkit.getScheduler().runTask(Main.instance, () -> {p.teleport(npc.getStoredLocation());});
                                p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getDidNotFinishInTime(), subRun.getRun(), subRun.getLobby()));
                            }
                        }
                    }

                    stopRunning();
                }
            }
        };
    }

    public List<Player> getPlayers() {
        return players;
    }

    public SubRun getSubRun() {
        return subRun;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentLevelIndex() { return currentLevelIndex; }


    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) { this.players.remove(player); }

    public void increaseCurrentLevel() {
        this.currentLevelIndex++;
        this.currentLevel = subRun.getLevels().get(currentLevelIndex);
    }

    public void setRunning(boolean isRunning) { this.isRunning = isRunning; }


    public void startRunning() {
        this.isRunning = true;
        bossBar.setProgress(1.0);
        this.secondsLeft = maxSeconds;
        timer.runTaskTimer(Main.instance, 0L, 20L);

        for (Player p : players) bossBar.addPlayer(p);
    }

    public void stopRunning() {
        bossBar.removeAll();
        this.isRunning = false;
        this.players.clear();
        timer.cancel();
        Utils utils = new Utils();
        MessageHelper lang = new MessageHelper(Main.getMobrunConfig());

        this.timer = new BukkitRunnable() {
            @Override
            public void run() {
                setSecondsLeft(getSecondsLeft()-1);

                bossBar.setProgress((double)secondsLeft / (double)maxSeconds);
                bossBar.setTitle(utils.replaceRunAndLobbyPlaceholders(subRun.getRun().getBossBarTitle(), subRun.getRun(), subRun.getLobby()));

                if (bossBar.getProgress() > 0.66) {
                    bossBar.setColor(BarColor.GREEN);
                } else if (bossBar.getProgress() > 0.33) {
                    bossBar.setColor(BarColor.YELLOW);
                } else {
                    bossBar.setColor(BarColor.RED);
                }

                if (getSecondsLeft() == 0) {

                    // Get location to TP back to
                    for (NPC npc : CitizensAPI.getNPCRegistry()) {
                        if (npc.getName().equals(subRun.getRun().getNpcName())) {
                            for (Player p : getPlayers()) {
                                // Teleporting on main thread so bukkit doesn't shout at me
                                Bukkit.getScheduler().runTask(Main.instance, () -> {p.teleport(npc.getStoredLocation());});
                                p.sendMessage(utils.replaceRunAndLobbyPlaceholders(lang.getDidNotFinishInTime(), subRun.getRun(), subRun.getLobby()));
                            }
                        }
                    }

                    stopRunning();
                }
            }
        };
    }


    public boolean isRunning() {
        return isRunning;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public void fixBossBar() {
        bossBar.removeAll();
        for (Player p : players) bossBar.addPlayer(p);
    }

    public void swapPlayer(Player original, Player swapTo) {
        players.remove(original);
        players.add(swapTo);
    }
}
