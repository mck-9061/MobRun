package me.therealmck.mobrun.stuff;

import org.bukkit.entity.Player;

import java.util.List;

public class Lobby {
    private List<Player> players;
    private final SubRun subRun;
    private Level currentLevel;
    private boolean isRunning;

    public Lobby(SubRun subRun) {
        this.subRun = subRun;
        this.isRunning = false;
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


    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) { this.players.remove(player); }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }


    public void startRunning() {
        this.isRunning = true;
    }

    public void stopRunning() {
        this.isRunning = false;
    }


    public boolean isRunning() {
        return isRunning;
    }
}
