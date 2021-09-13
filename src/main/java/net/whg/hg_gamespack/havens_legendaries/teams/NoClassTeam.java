package net.whg.hg_gamespack.havens_legendaries.teams;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import net.whg.hg_gamespack.havens_legendaries.HavensLegendaries;
import net.whg.hg_gamespack.havens_legendaries.classes.ClassType;
import net.whg.hg_gamespack.havens_legendaries.events.AllPlayersHaveClassesEvent;
import net.whg.minigames.framework.events.LeaveMinigameEvent;
import net.whg.minigames.framework.events.MinigameReadyEvent;
import net.whg.minigames.framework.teams.Team;
import net.whg.minigames.framework.utilities.FloaterSelectedEvent;
import net.whg.minigames.framework.utilities.ModeledSelectionFloater;
import net.whg.minigames.framework.utilities.SelectionFloater;
import net.whg.utils.messaging.MessageUtils;

/**
 * The team that all players are part of before choosing a class or being
 * assigned to a real team.
 */
public class NoClassTeam extends Team {
    private final Map<Player, ClassType> selectedClasses = new HashMap<>();
    private final Map<SelectionFloater, ClassType> floaters = new HashMap<>();
    private final Map<ClassType, Location> floaterLocations = new HashMap<>();
    private final HavensLegendaries minigame;

    /**
     * Creates a new NoClassTeam instance.
     * 
     * @param minigame - The minigame instance this team is for.
     */
    public NoClassTeam(HavensLegendaries minigame) {
        this.minigame = minigame;
    }

    @Override
    public String getName() {
        return "no class";
    }

    @EventHandler
    public void onMinigameReady(MinigameReadyEvent e) {
        if (e.getMinigame() != minigame)
            return;

        for (var classType : floaterLocations.keySet())
            spawnFloater(classType);
    }

    /**
     * Spawns a selection floater at the correct location for the given class type.
     * 
     * @param classType - The class type.
     */
    private void spawnFloater(ClassType classType) {
        var location = floaterLocations.get(classType);
        var floater = new ModeledSelectionFloater(location, classType.getName(), classType.getModel());
        floaters.put(floater, classType);
    }

    @EventHandler
    public void onFloaterSelected(FloaterSelectedEvent e) {
        var player = e.getPlayer();
        var floater = e.getSelectionFloater();

        if (!floaters.containsKey(floater))
            return;

        var classType = floaters.get(floater);
        selectedClasses.put(player, classType);
        MessageUtils.sendMessage(player, "You have selected the %s class.", classType.getName());

        tryAssignTeams();
    }

    @EventHandler
    public void onPlayerLeaveMinigame(LeaveMinigameEvent e) {
        selectedClasses.remove(e.getPlayer());
        tryAssignTeams();
    }

    /**
     * Checks if all players have selected their classes. If so, it triggers the
     * corresponding event.
     */
    private void tryAssignTeams() {
        if (selectedClasses.size() != getPlayers().size())
            return;

        Bukkit.getPluginManager().callEvent(new AllPlayersHaveClassesEvent(minigame));
    }

    /**
     * Sets the selection floater location for the given class type.
     * 
     * @param classType - The class type.
     * @param location  - The floater location.
     */
    public void setFloaterLocation(ClassType classType, Location location) {
        floaterLocations.put(classType, location);
    }
}
