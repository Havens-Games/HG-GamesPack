package net.whg.hg_gamespack.pac_man_lights_out;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.events.MinigameReadyEvent;
import net.whg.minigames.framework.teams.Team;
import net.whg.minigames.framework.teams.TeamUtils;
import net.whg.utils.math.Vec3;

public class PacManLightsOut extends Minigame {
    private final PacManTeam pacmanTeam = new PacManTeam(this);
    private final GhostTeam ghostTeam = new GhostTeam(this);

    PacManLightsOut() {
        var teamList = getTeamList();
        teamList.createTeam(pacmanTeam);
        teamList.createTeam(ghostTeam);
    }

    @EventHandler
    public void onReady(MinigameReadyEvent e) {
        if (e.getMinigame() != this)
            return;

        pacmanTeam.setSpawnPoints(findPlaceholders(ChatColor.DARK_AQUA + "PacMan Spawn Point"));
        ghostTeam.setSpawnPoints(findPlaceholders(ChatColor.DARK_AQUA + "Ghost Spawn Point"));
        ghostTeam.setJail(findPlaceholders(ChatColor.DARK_AQUA + "Ghost Jail").get(0));

        pacmanTeam.setBlocksRemaining(countBlocksToBreak());

        var maxSize = new HashMap<Team, Integer>();
        maxSize.put(getTeamList().getTeam("pacman"), 1);
        maxSize.put(getTeamList().getTeam("ghost"), 3);
        TeamUtils.shuffleTeams(getPlayers(), getTeamList().getTeams(), maxSize);

        pacmanTeam.initialize();
        ghostTeam.initialize();
    }

    /**
     * When a player's food level changes, this event listener cancels the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }

    /**
     * When a player from this minigame breaks a block, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!getPlayers().contains(e.getPlayer()))
            return;

        e.setCancelled(true);
    }

    /**
     * When a player from this minigame drops an item, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onItemDrop(EntityDropItemEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPacManWin(PacManWinEvent e) {
        if (e.getMinigame() != this)
            return;

        for (var p : getPlayers())
            p.sendMessage(ChatColor.GREEN + "Pacman wins!");

        endGame();
    }

    @EventHandler
    public void onGhostWin(GhostWinEvent e) {
        if (e.getMinigame() != this)
            return;

        for (var p : getPlayers())
            p.sendMessage(ChatColor.GREEN + "Ghosts win!");

        endGame();
    }

    /**
     * Triggers this game instance to end.
     */
    private void endGame() {
        for (var p : new ArrayList<>(getPlayers())) {
            p.sendMessage(ChatColor.GREEN + "The game has ended!");
            removePlayer(p);
        }
    }

    /**
     * Count all of the sear lantern or glowstone blocks that are present within the
     * arena that need to be broken by pacman.
     * 
     * @return The number of blocks to break.
     */
    private int countBlocksToBreak() {
        var schematic = getArena().getSchematic();
        var location = getArena().getLocation();
        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var offset = loc.subtract(schematic.getOrigin());
        var world = location.getWorld();

        var min = schematic.getMinimumPoint().add(offset);
        var max = schematic.getMaximumPoint().add(offset);

        var blocksRemaining = 0;
        for (var x = min.x; x <= max.x; x++) {
            for (var y = min.y; y <= max.y; y++) {
                for (var z = min.z; z <= max.z; z++) {
                    var block = world.getBlockAt(x, y, z);
                    var type = block.getType();
                    if (type == Material.SEA_LANTERN || type == Material.GLOWSTONE)
                        blocksRemaining++;
                }
            }
        }

        return blocksRemaining;
    }
}
