package net.whg.hg_gamespack.havens_legendaries.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.hg_gamespack.havens_legendaries.HavensLegendaries;

/**
 * Called when all players within a Haven's Legendaries minigame instance have
 * selected their classes.
 */
public class AllPlayersHaveClassesEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final HavensLegendaries minigame;

    /**
     * Creates a new AllPlayersHaveClassesEvent.
     * 
     * @param minigame - The minigame instance.
     */
    public AllPlayersHaveClassesEvent(HavensLegendaries minigame) {
        this.minigame = minigame;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the minigame instance that was involved in this event.
     * 
     * @return The minigame instance.
     */
    public HavensLegendaries getMinigame() {
        return minigame;
    }
}
