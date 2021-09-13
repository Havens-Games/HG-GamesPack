package net.whg.hg_gamespack.havens_legendaries.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.teams.Team;

public abstract class InFieldTeam extends Team {
    private final Minigame minigame;
    private final List<Location> spawnPoints = new ArrayList<>();

    protected InFieldTeam(Minigame minigame) {
        this.minigame = minigame;
    }

    /**
     * Sets the spawn points that are used by this team.
     * 
     * @param spawnPoints - A list of spawn point locations.
     */
    public void setSpawnPoints(List<Location> spawnPoints) {
        this.spawnPoints.clear();
        this.spawnPoints.addAll(spawnPoints);
    }
}
