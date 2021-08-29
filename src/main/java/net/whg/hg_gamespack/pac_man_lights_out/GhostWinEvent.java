package net.whg.hg_gamespack.pac_man_lights_out;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.Minigame;

/**
 * Called when the ghost team successfully kills pacman until they run out of
 * lives before completing their objective.
 */
public class GhostWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Minigame minigame;

    /**
     * Creates a new GhostWinEvent.
     * 
     * @param minigame - The minigame instance where this event originated.
     */
    public GhostWinEvent(Minigame minigame) {
        this.minigame = minigame;
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
}
