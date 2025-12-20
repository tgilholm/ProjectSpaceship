package org.tom.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The parent class for the game entities. Defines properties common to all entities.
 */
public abstract class Entity
{
    // Declared protected so all inheritors can access the logger
    protected static final Logger logger = LogManager.getLogger();
    protected final double maxHealth;
    protected final double maxDefenceStrength;

    protected double health;
    protected double defenceStrength;
    protected Sector sector;


    /**
     * Sets the base values for an Entity. Health and DefenseStrength are initially set to maximum
     *
     * @param maxHealth          the maximum total health
     * @param maxDefenceStrength the maximum resistance to damage
     * @param sector             the starting position of the entity
     */
    public Entity(double maxHealth, double maxDefenceStrength, Sector sector)
    {
        this.maxHealth = maxHealth;
        this.maxDefenceStrength = maxDefenceStrength;
        this.sector = sector;

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


    public double getHealth()
    {
        return health;
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
    void setHealth(double newHealth)
    {
        this.health = Math.max(0.0, Math.min(newHealth, maxHealth));
    }


    /**
     * Subtracts the incoming damage, reduced by the <code>defenceStrength</code> of this entity.
     * Prevents <code>health</code> from falling below 0.
     *
     * @param damage the incoming damage, as a <code>double</code>
     */
    public void takeDamage(double damage)
    {
        /*
        If damage minus defenceStrength results in a negative number,
        take away 0 health instead of adding health
         */
        this.health -= (health - Math.max(0.0, damage - getDefenceStrength()));
    }


}
