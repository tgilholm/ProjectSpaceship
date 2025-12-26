package org.tom.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    protected final static Logger logger = LogManager.getLogger();


    /**
     * Overrides <code>toString</code> from <code>Object</code> superclass
     *
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
     *
     * @param target the <code>Entity</code> to attack
     */
    public void attackWithAll(Entity target)
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
            } else if (e instanceof Starbase)
            {
                starbases.add((Starbase) e);    // Add to the starbase list
            }
            logger.info("Adding {} to fleet {}", e, this);
        }
    }


    /**
     * Gets the specific <code>Starbase</code> object at the specified index.
     *
     * @return an <code>Optional</code> object with a <code>Starbase</code> if found, or empty otherwise
     */
    public Optional<Starbase> getStarbaseAt(int index)
    {
        // If the index is negative or larger than the starbase list size, return empty
        if (index < 0 || index >= starbases.size()) return Optional.empty();
        return Optional.of(starbases.get(index)); // Otherwise return the starbase
    }


    /**
     * Gets the specific <code>Starship</code> object at the specified index
     *
     * @return an <code>Optional</code> object with a <code>Starship</code> if found, or empty otherwise
     */
    public Optional<Starship> getStarshipAt(int index)
    {
        if (index < 0 || index >= starships.size()) return Optional.empty();
        return Optional.of(starships.get(index));
    }


    /**
     * Docks the specified <code>Starship</code> objects to a <code>Starbase</code>
     *
     * @param starbase  the <code>Starbase</code> to dock to
     * @param starships the <code>Starship</code> objects to dock (varargs, any no of starships)
     */
    public void dockStarshipsTo(Starbase starbase, Starship @NonNull ... starships)
    {
        for (Starship s : starships)
        {
            if (!this.starships.contains(s) || !starbases.contains(starbase))
            {
                logger.info("Cannot dock: ship {} or base {} not in {}", s, starbase, this);
            } else
            {
                s.dockToStarbase(starbase);
            }
        }
    }
}
