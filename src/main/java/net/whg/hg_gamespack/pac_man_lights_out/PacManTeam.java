package net.whg.hg_gamespack.pac_man_lights_out;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.teams.Team;

class PacManTeam extends Team {
    private final Minigame minigame;
    private final Random random = new Random();
    private final List<Location> spawnPoints = new ArrayList<>();
    private int livesLeft = 3;
    private int blocksRemaining;

    public PacManTeam(Minigame minigame) {
        this.minigame = minigame;
    }

    void setSpawnPoints(List<Location> spawnPoints) {
        this.spawnPoints.clear();
        this.spawnPoints.addAll(spawnPoints);
    }

    void setBlocksRemaining(int blocksRemaining) {
        this.blocksRemaining = blocksRemaining;
    }

    void initialize() {
        for (var player : getPlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, true, false, true));
            randomSpawn(player);
        }
    }

    @Override
    public String getName() {
        return "pacman";
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        if (!getPlayers().contains(player))
            return;

        e.setCancelled(true);
        player.setHealth(20);
        livesLeft--;

        Bukkit.getPluginManager().callEvent(new PacManDeathEvent(minigame, player, livesLeft));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        if (!getPlayers().contains(player))
            return;

        var loc = player.getLocation();
        var world = loc.getWorld();
        var x = loc.getBlockX();
        var y = loc.getBlockY();
        var z = loc.getBlockZ();

        for (var a = -1; a <= 1; a++) {
            for (var b = 2; b <= 3; b++) {
                for (var c = -1; c <= 1; c++) {
                    tryRemoveBlock(world, x + a, y + b, z + c, player);
                }
            }
        }
    }

    @EventHandler
    public void onPacManDeath(PacManDeathEvent e) {
        var player = e.getPlayer();
        if (!getPlayers().contains(player))
            return;

        if (livesLeft > 0) {
            player.sendMessage(ChatColor.GREEN.toString() + livesLeft + " lives remaining!");
            randomSpawn(player);
        } else {
            Bukkit.getPluginManager().callEvent(new GhostWinEvent(minigame));
        }
    }

    @EventHandler
    public void onPacManPowerUp(PacManPowerUpEvent e) {
        var player = e.getPlayer();
        if (!getPlayers().contains(player))
            return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 1, true, false, true));
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayers().isEmpty())
                    return;

                player.getInventory().clear();
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HG-Minigames"), 30 * 20);
    }

    private void tryRemoveBlock(World world, int x, int y, int z, Player player) {
        var block = world.getBlockAt(x, y, z);

        if (block.getType() == Material.SEA_LANTERN) {
            removeBlock(block, player);
        } else if (block.getType() == Material.GLOWSTONE) {
            Bukkit.getPluginManager().callEvent(new PacManPowerUpEvent(minigame, player));
            removeBlock(block, player);
        }
    }

    private void removeBlock(Block block, Player player) {
        block.setType(Material.AIR);
        block.getRelative(BlockFace.UP, 2).setType(Material.AIR);
        blocksRemaining -= 2;

        if (blocksRemaining == 0) {
            Bukkit.getPluginManager().callEvent(new PacManWinEvent(minigame, player));
        }
    }

    private void randomSpawn(Player player) {
        var spawn = spawnPoints.get(random.nextInt(spawnPoints.size()));
        player.teleport(spawn);
    }
}
