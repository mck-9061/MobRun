package me.therealmck.mobrun.stuff;

import org.bukkit.entity.Player;

import java.util.List;

public class Lobby {
    private List<Player> players;
    private final SubRun subRun;
    private Level currentLevel;
    private int currentLevelIndex;
    private boolean isRunning;
    private int secondsLeft;

    public Lobby(SubRun subRun) {
        this.subRun = subRun;
        this.isRunning = false;
        this.secondsLeft = 0;
        this.currentLevel = subRun.getLevels().get(0);
        this.currentLevelIndex = 0;
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


    public void startRunning() {
        this.isRunning = true;
    }

    public void stopRunning() {
        this.isRunning = false;
        this.players.clear();
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
}
