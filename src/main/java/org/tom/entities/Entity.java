package org.tom.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The parent class for the game entities. Defines properties common to all entities.
 */
public abstract class Entity
{
    protected static final Logger logger = LogManager.getLogger();
    private static final AtomicLong NEXT_ID = new AtomicLong(1); // Used for entity IDs
    protected final double maxHealth;
    protected final double maxDefenceStrength;
    private Fleet fleet;      // The fleet this entity is in
    private final long id;

    protected double health;
    protected double defenceStrength;
    protected Sector sector;
    protected boolean destroyed;     // If this entity has been destroyed


    /**
     * Sets the base values for an Entity. Health and DefenseStrength are initially set to maximum.
     * Gets the next value from NEXT_ID and sets it to this Entity's ID.
     *
     * @param maxHealth          the maximum total health
     * @param maxDefenceStrength the maximum resistance to damage
     * @param sector             the starting position of the entity
     */
    public Entity(double maxHealth, double maxDefenceStrength, Sector sector)
    {
        this.id = NEXT_ID.getAndIncrement();    // Get the id from the AtomicLong and increment for uniqueness
        this.maxHealth = maxHealth;
        this.maxDefenceStrength = maxDefenceStrength;
        this.sector = sector;
        this.fleet = null;      // No fleet alignment by default

        // Set values to maximum initially
        health = maxHealth;
        defenceStrength = maxDefenceStrength;
    }


    /**
     * Calculates the current defence strength of this entity. Declared <code>abstract</code>
     * as entities have different methods for calculating defence strength
     *
     * @return a <code>double</code> representing the current defence strength
     */
    public abstract double getDefenceStrength();


    /**
     * Gets the remaining health of this entity
     *
     * @return a <code>double</code> for the health
     */
    public double getHealth()
    {
        return health;
    }


    /**
     * Gets the fleet this entity is a part of
     *
     * @return a <code>Fleet</code> object
     */
    public Fleet getFleet()
    {
        return fleet;
    }


    /**
     * Returns the <code>Sector</code> object containing the coordinates of this <code>Entity</code>
     *
     * @return a <code>Sector</code> object
     */
    public Sector getSector()
    {
        return sector;
    }


    /**
     * Sets the health of this <code>Entity</code> from a provided <code>newHealth</code>. Prevents
     * negative health by selecting the largest value from 0 or newHealth- if newHealth is negative,
     * health is set to 0. Uses <code>Math.min</code> to prevent health values above maximum
     *
     * @param newHealth the new health <code>double</code>
     */
    public void setHealth(double newHealth)
    {
        this.health = Math.max(0.0, Math.min(newHealth, maxHealth));

        if (this.health == 0)
        {
            this.destroyed = true;
            logger.info("{} has been destroyed", this);
        }
    }


    /**
     * Used to assign fleet ownership to entities. Not in the constructor to avoid long argument lists
     *
     * @param fleet the fleet that owns this <code>Entity</code>
     */
    public void setFleet(Fleet fleet)
    {
        this.fleet = fleet;
    }


    /**
     * Subtracts the incoming damage, reduced by the <code>defenceStrength</code> of this entity.
     * Prevents <code>health</code> from falling below 0.
     *
     * @param damage the incoming damage, as a <code>double</code>
     */
    public void takeDamage(double damage)
    {
        if (!destroyed)
        {
            // Incoming damage is damage-defenceStrength, or 5, whichever is higher
            // If damage - defence strength < 5, the damage applied is 5.
            // If the damage is higher than the remaining health, health is set to 0.
            double appliedDamage = Math.min(Math.max(5, damage - getDefenceStrength()), this.health);

            this.setHealth(health - appliedDamage);

            logger.debug("{} taking {} damage. Remaining health: {}", this, appliedDamage, getHealth());
        } else
        {
            logger.info("{} has been destroyed. Taking 0 damage", this);
        }
    }


    /**
     * Overrides <code>toString</code> in <code>Object</code>. Outputs the class name
     * and unique ID for logging
     *
     * @return a <code>String</code> output of this object
     */
    @Override
    public String toString()
    {
        // Gets the class name (e.g. Starship), then appends "#id" where id is the unique id
        return "{" + getFleet() + "} " + getClass().getSimpleName() + "#" + id;
    }
}
