package org.tom.entities;

import java.util.List;

/**
 * The Fleet object serves to associate player ownership with a group of Entities
 * It holds a <code>entities.Player</code> value and a <code>List</code> of <code>Entity</code> objects,
 * comprising both Starbases and Starships
 */
public class Fleet
{
    private final Player player;
    private final List<Starbase> starbases;
    private final List<Starship> starships;


    /**
     * Sets the base values for a Fleet and initializes the <code>entityList</code>
     *
     * @param player    the Player who owns this Fleet
     * @param starbases a list of <code>Starbase</code> objects
     * @param starships a list of <code>Starship</code> objects
     */
    public Fleet(Player player, List<Starbase> starbases, List<Starship> starships)
    {
        this.player = player;
        this.starbases = starbases;
        this.starships = starships;
    }


    /**
     * Moves all the <code>Starship</code> entities in the <code>starships</code> list to the given sector.
     * Docked starships are not moved
     */
    public void moveAllEntities(Sector newSector)
    {
        for (Starship starship : starships)
        {
            starship.setSector(newSector);
        }
    }


    /**
     * Commands all <code>Starship</code> entities to attack a target <code>Entity</code>. Docked starships will
     * not be moved, and the attack will only take place if the target is in the same sector as the fleet and of
     * an opposing fleet.
     */
    public void attackTarget(Entity target)
    {
        for (Starship starship : starships)
        {
            starship.attack(target);
        }
    }
}
