package net.whg.hg_gamespack.havens_legendaries;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.MinigameFactory;

public class HavensLegendariesFactory implements MinigameFactory {

    @Override
    public Minigame createInstance() {
        return new HavensLegendaries();
    }

    @Override
    public String getName() {
        return "Haven's Legendaries";
    }

    @Override
    public boolean isInstanced() {
        return true;
    }

    @Override
    public int getMinPlayers() {
        return 8;
    }

    @Override
    public int getMaxPlayers() {
        return 30;
    }
}
