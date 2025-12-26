package org.tom.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
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
    private final static Logger logger = LogManager.getLogger();


    /**
     * Overrides <code>toString</code> from <code>Object</code> superclass
     * @return the fleet name including the player number
     */
    @Override
    public String toString()
    {
        return "Fleet#" + player.playerNo();
    }

    /**
     * Sets the base values for a Fleet and initializes the entity lists
     *
     * @param player the Player who owns this Fleet
     */
    public Fleet(Player player)
    {
        this.player = player;

        // Initialize the lists
        starbases = new ArrayList<>();
        starships = new ArrayList<>();
    }


    /**
     * Moves all the <code>Starship</code> entities in the <code>starships</code> list to the given sector.
     * Docked starships are not moved
     */
    public void moveAllEntities(Sector newSector)
    {
        logger.debug("Moving {} starships from {} to sector {}", starships.size(), this, newSector);

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


    /**
     * Adds entities to this fleet. Separates the list of entities into <code>Starbase</code> and <code>Starship</code>
     * objects and adds them to the corresponding lists
     *
     * @param entities a varargs list of <code>Entity</code> objects taking any number of objects
     */
    public void addEntities(Entity @NonNull ... entities)
    {
        for (Entity e : entities)
        {
            /*
            Add starship entities to the starship list and starbases to the starbase list
            First check the class type, then safely cast to the corresponding type.
            Sets the Fleet variable of the entity to this.
             */
            e.setFleet(this);

            if (e instanceof Starship)
            {
                starships.add((Starship) e);    // Add to the starship l
                logger.info("Adding {} to fleet {}", e, this);
            } else if (e instanceof Starbase)
            {
                starbases.add((Starbase) e);    // Add to the starbase list
            }
        }
    }


    /**
     * Get the starbases in this fleet
     * @return a list of <code>Starbase</code> objects
     */
    public List<Starbase> getStarbases()
    {
        return starbases;
    }

    /**
     * Get the starships in this fleet
     * @return a list of <code>Starship</code> objects
     */
    public List<Starship> getStarships()
    {
        return starships;
    }
}
