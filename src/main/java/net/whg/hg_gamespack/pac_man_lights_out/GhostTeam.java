package net.whg.hg_gamespack.pac_man_lights_out;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.teams.Team;

public class GhostTeam extends Team {
    private final Minigame minigame;
    private final List<Location> spawnPoints = new ArrayList<>();
    private Location jail;

    public GhostTeam(Minigame minigame) {
        this.minigame = minigame;
    }

    void setSpawnPoints(List<Location> spawnPoints) {
        this.spawnPoints.clear();
        this.spawnPoints.addAll(spawnPoints);
    }

    void setJail(Location jail) {
        this.jail = jail;
    }

    void initialize() {
        int spawnIndex = 0;
        for (var player : getPlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false, true));
            player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
            player.teleport(spawnPoints.get(spawnIndex++));
        }
    }

    @Override
    public String getName() {
        return "ghost";
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        if (!getPlayers().contains(player))
            return;

        e.setCancelled(true);
        player.setHealth(20);
        player.teleport(jail);
        player.sendMessage("You have died! Respawning in 8 seconds.");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayers().isEmpty())
                    return;

                player.teleport(spawnPoints.get(0));
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HG-GamesPack"), 8 * 20L);
    }

    @EventHandler
    public void onPacManPowerUp(PacManPowerUpEvent e) {
        if (e.getMinigame() != minigame)
            return;

        for (var p : getPlayers()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30 * 20, 0, true, false, true));
        }
    }
}
