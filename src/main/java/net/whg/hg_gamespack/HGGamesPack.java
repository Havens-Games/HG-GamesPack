package net.whg.hg_gamespack;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.whg.hg_gamespack.jump_pad.JumpPadFactory;
import net.whg.hg_gamespack.pac_man_lights_out.PacManLightsOutFactory;
import net.whg.minigames.MinigamesPlugin;

public class HGGamesPack extends JavaPlugin {
    @Override
    public void onEnable() {
        var plugin = (MinigamesPlugin) Bukkit.getPluginManager().getPlugin("Minigames");
        var minigameManager = plugin.getMinigameManager();
        minigameManager.registerMinigameType(new JumpPadFactory());
        minigameManager.registerMinigameType(new PacManLightsOutFactory());
    }
}
