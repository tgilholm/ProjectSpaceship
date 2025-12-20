package org.tom.entities;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The parent class for the game entities. Defines properties common to all entities.
 */
public abstract class Entity
{
    protected final double maxHealth;
    protected final double maxDefenceStrength;
    private static final AtomicLong NEXT_ID = new AtomicLong(1); // Used for entity IDs
    private final long id;

    protected double health;
    protected double defenceStrength;
    protected Sector position;


    /**
     * Sets the base values for an Entity. Health and DefenseStrength are initially set to maximum.
     * Gets the next value from NEXT_ID and sets it to this Entity's ID.
     *
     * @param maxHealth          the maximum total health
     * @param maxDefenceStrength the maximum resistance to damage
     * @param position           the starting position of the entity
     */
    public Entity(double maxHealth, double maxDefenceStrength, Sector position)
    {
        this.id = NEXT_ID.getAndIncrement();    // Get the id from the AtomicLong and increment for uniqueness
        this.maxHealth = maxHealth;
        this.maxDefenceStrength = maxDefenceStrength;
        this.position = position;

        // Set values to maximum initially
        health = maxHealth;
        defenceStrength = maxDefenceStrength;
    }


    /**
     * Returns the unique id of this <code>Entity</code>
     *
     * @return a <code>long</code> id
     */
    public long getId()
    {
        return id;
    }


    /**
     * Overrides <code>toString</code> in <code>Object</code>. Outputs the class name
     * and unique ID for logging
     * @return a <code>String</code> output of this object
     */
    @Override
    public String toString()
    {
        // Gets the class name (e.g. Starship), then appends "#id" where id is the unique id
        return getClass().getSimpleName() + "#" + id;
    }


    /**
     * Calculates the current defence strength of this entity
     *
     * @return a <code>double</code> representing the current defence strength
     */
    public abstract double getDefenceStrength();
}
