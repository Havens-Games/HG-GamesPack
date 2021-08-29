package net.whg.hg_gamespack.pac_man_lights_out;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.Minigame;

/**
 * this event is called whenever PacMan is killed.
 */
public class PacManDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Minigame minigame;
    private final Player player;
    private final int livesLeft;

    /**
     * Creates a new PacManDeathEvent.
     * 
     * @param minigame  - The minigame instance where this event originated.
     * @param player    - The pacman player that died.
     * @param livesLeft - The remaining number of pacman lives.
     */
    public PacManDeathEvent(Minigame minigame, Player player, int livesLeft) {
        this.minigame = minigame;
        this.player = player;
        this.livesLeft = livesLeft;
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

    /**
     * Gets the number of remaining lives for pacman.
     * 
     * @return The lives left.
     */
    public int getLivesLeft() {
        return livesLeft;
    }
}
