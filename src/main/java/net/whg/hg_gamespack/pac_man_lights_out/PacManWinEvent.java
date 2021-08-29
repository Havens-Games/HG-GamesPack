package net.whg.hg_gamespack.pac_man_lights_out;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.Minigame;

/**
 * Called when PacMan collects all of the light source blocks before running out
 * of lives.
 */
public class PacManWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Minigame minigame;
    private final Player player;

    /**
     * Creates a new PacManWinEvent.
     * 
     * @param minigame - The minigame instance where this event originated.
     * @param player   - The pacman player that died.
     */
    public PacManWinEvent(Minigame minigame, Player player) {
        this.minigame = minigame;
        this.player = player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the minigame instance that triggered this event.
     * 
     * @return The minigame instance.
     */
    public Minigame getMinigame() {
        return minigame;
    }

    /**
     * Gets the player involved in this event.
     * 
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }
}
