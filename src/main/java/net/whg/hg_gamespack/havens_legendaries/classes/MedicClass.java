package net.whg.hg_gamespack.havens_legendaries.classes;

/**
 * A class that primarily focuses on healing other teammates.
 */
public class MedicClass implements ClassType {
    @Override
    public String getName() {
        return "Medic";
    }

    @Override
    public String getModel() {
        return "RedCross";
    }
}
