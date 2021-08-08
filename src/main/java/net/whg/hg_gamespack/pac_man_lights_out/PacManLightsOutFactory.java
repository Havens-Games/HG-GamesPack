package net.whg.hg_gamespack.pac_man_lights_out;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.MinigameFactory;

public class PacManLightsOutFactory implements MinigameFactory {
    @Override
    public Minigame createInstance() {
        return new PacManLightsOut();
    }

    @Override
    public String getName() {
        return "PacManLightsOut";
    }

    @Override
    public boolean isInstanced() {
        return true;
    }

    @Override
    public int getMinPlayers() {
        return 2; // TODO Set to 4
    }

    @Override
    public int getMaxPlayers() {
        return 2; // TODO Set to 4
    }
}
