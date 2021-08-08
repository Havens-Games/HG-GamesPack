package net.whg.hg_gamespack.pac_man_lights_out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.events.ArenaCompletedEvent;
import net.whg.utils.math.Vec3;

public class PacManLightsOut extends Minigame {
    private final List<Location> ghostSpawnPoints = new ArrayList<>();
    private final List<Location> pacmanSpawnPoints = new ArrayList<>();
    private Location ghostJail;
    private Player pacman;
    private int pacmanLivesLeft = 3;
    private int blocksRemaining;

    /**
     * Gives the player all the required potion effects and items required to play
     * the game.
     * 
     * @param player - The player.
     */
    private void prepareInventory(Player player) {
        if (player == pacman) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0, true, false, true));
            player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false, true));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        }
    }

    /**
     * Teleports the player to a random spawn point for this minigame.
     * 
     * @param player - The player.
     */
    private void teleportToSpawn() {
        var list = new ArrayList<Player>(getPlayers());
        Collections.shuffle(list);

        pacman = list.get(0);

        var pacmanSpawn = pacmanSpawnPoints.get(random.nextInt(pacmanSpawnPoints.size()));
        pacman.teleport(pacmanSpawn);

        for (var i = 1; i < list.size(); i++)
            list.get(i).teleport(ghostSpawnPoints.get(i));
    }

    /**
     * When the arena is finished being built, this event listener looks for any
     * spawn point placeholders to reference. This event listener will also start
     * and players that are currently waiting on this arena.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onArenaFinished(ArenaCompletedEvent e) {
        if (e.getArena() != getArena())
            return;

        findSpawnPoints();
        countBlocksToBreak();

        // TODO Make sure all players have joined the game before running this part.
        teleportToSpawn();
        for (var player : getPlayers()) {
            prepareInventory(player);
        }
    }

    /**
     * Search through the arena area to find all spawn locations via placeholders.
     */
    private void findSpawnPoints() {
        ghostSpawnPoints.addAll(findPlaceholders(ChatColor.DARK_AQUA + "Ghost Spawn Point"));
        pacmanSpawnPoints.addAll(findPlaceholders(ChatColor.DARK_AQUA + "PacMan Spawn Point"));
        ghostJail = findPlaceholders(ChatColor.DARK_AQUA + "Ghost Jail").get(0);

        // TODO Assert all spawn points have been loaded.
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
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        var player = e.getEntity();
        e.setCancelled(true);
        player.setHealth(20);

        if (player == pacman) {
            pacmanLivesLeft--;

            if (pacmanLivesLeft == 0) {
                ghostWin();
            } else {
                var pacmanSpawn = pacmanSpawnPoints.get(random.nextInt(pacmanSpawnPoints.size()));
                pacman.teleport(pacmanSpawn);

                player.sendMessage(ChatColor.GREEN.toString() + pacmanLivesLeft + " lives remaining!");
            }
        } else {
            ghostRespawn(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        if (player != pacman)
            return;

        var loc = player.getLocation();
        var block = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 3, loc.getBlockZ());

        if (block.getType() == Material.SEA_LANTERN) {
            removeBlock(block);
        } else if (block.getType() == Material.GLOWSTONE) {
            var gameStillActive = removeBlock(block);
            if (gameStillActive)
                powerup();
        }
    }

    private boolean removeBlock(Block block) {
        block.setType(Material.AIR);
        block.getRelative(BlockFace.UP, 2).setType(Material.AIR);
        blocksRemaining--;

        if (blocksRemaining == 0) {
            pacmanWin();
            return false;
        }

        return true;
    }

    private void powerup() {
        for (var p : getPlayers()) {
            if (p == pacman)
                continue;

            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30 * 20, 0, true, false, true));
        }

        pacman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 1, true, false, true));
        pacman.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 1, true, false, true));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayers().isEmpty())
                    return;

                pacman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0, true, false, true));
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HG-Minigames"), 30 * 20);
    }

    private void ghostWin() {
        for (var p : getPlayers())
            p.sendMessage(ChatColor.GREEN + "Ghosts win!");

        endGame();
    }

    private void pacmanWin() {
        for (var p : getPlayers())
            p.sendMessage(ChatColor.GREEN + "Pacman wins!");

        endGame();
    }

    private void endGame() {
        for (var p : new ArrayList<>(getPlayers())) {
            p.sendMessage(ChatColor.GREEN + "The game has ended!");
            removePlayer(p);
        }
    }

    private void ghostRespawn(Player player) {
        player.teleport(ghostJail);
        player.sendMessage("You have died! Respawning in 8 seconds.");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayers().isEmpty())
                    return;

                player.teleport(ghostSpawnPoints.get(0));
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HG-Minigames"), 8 * 20);
    }

    private void countBlocksToBreak() {
        var schematic = getArena().getSchematic();
        var location = getArena().getLocation();
        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var offset = loc.subtract(schematic.getOrigin());
        var world = location.getWorld();

        var min = schematic.getMinimumPoint().add(offset);
        var max = schematic.getMaximumPoint().add(offset);

        blocksRemaining = 0;
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
    }
}
