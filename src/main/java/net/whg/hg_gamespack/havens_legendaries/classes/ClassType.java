package net.whg.hg_gamespack.havens_legendaries.classes;

/**
 * Represents a selectable class type within Havens Legendaries.
 */
public interface ClassType {
    /**
     * Gets the name of this class type.
     * 
     * @return The class name.
     */
    String getName();

    /**
     * Gets the name of the selectable, floating model that is associated with this
     * class.
     * 
     * @return The model name.
     */
    String getModel();
}
