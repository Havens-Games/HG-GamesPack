package net.whg.hg_gamespack.havens_legendaries;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import net.whg.hg_gamespack.havens_legendaries.classes.MedicClass;
import net.whg.hg_gamespack.havens_legendaries.events.AllPlayersHaveClassesEvent;
import net.whg.hg_gamespack.havens_legendaries.teams.BlueTeam;
import net.whg.hg_gamespack.havens_legendaries.teams.NoClassTeam;
import net.whg.hg_gamespack.havens_legendaries.teams.RedTeam;
import net.whg.minigames.framework.DefaultMinigame;
import net.whg.minigames.framework.events.MinigameReadyEvent;
import net.whg.minigames.framework.teams.TeamUtils;

public class HavensLegendaries extends DefaultMinigame {
    private final NoClassTeam noClassTeam = new NoClassTeam(this);
    private final RedTeam redTeam = new RedTeam(this);
    private final BlueTeam blueTeam = new BlueTeam(this);

    public HavensLegendaries() {
        var teamList = getTeamList();
        teamList.createTeam(noClassTeam);
        teamList.createTeam(redTeam);
        teamList.createTeam(blueTeam);

        setDefaultTeam(noClassTeam);
    }

    @EventHandler
    public void onReady(MinigameReadyEvent e) {
        if (e.getMinigame() != this)
            return;

        var spawnLocation = findPlaceholders(ChatColor.DARK_AQUA + "Spawn Point").get(0);
        teleportAll(spawnLocation);

        var redSpawnPoints = findPlaceholders(ChatColor.DARK_AQUA + "Red Spawn Point");
        redTeam.setSpawnPoints(redSpawnPoints);

        var blueSpawnPoints = findPlaceholders(ChatColor.DARK_AQUA + "Blue Spawn Point");
        blueTeam.setSpawnPoints(blueSpawnPoints);

        var medicClassLocation = findPlaceholders(ChatColor.DARK_AQUA + "Medic Class Selector").get(0);
        noClassTeam.setFloaterLocation(new MedicClass(), medicClassLocation);
    }

    @EventHandler
    public void onAllPlayersHaveClasses(AllPlayersHaveClassesEvent e) {
        if (e.getMinigame() != this)
            return;

        TeamUtils.shuffleTeams(getPlayers(), List.of(redTeam, blueTeam));
    }
}