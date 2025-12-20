package org.tom.entities;

/**
 * The parent class for the game entities. Defines properties common to all entities.
 */
public abstract class Entity
{
    protected final double maxHealth;
    protected final double maxDefenceStrength;

    protected double health;
    protected double defenceStrength;
    protected Sector position;


    /**
     * Sets the base values for an Entity. Health and DefenseStrength are initially set to maximum
     * @param maxHealth the maximum total health
     * @param maxDefenceStrength the maximum resistance to damage
     * @param position the starting position of the entity
     */
    public Entity(double maxHealth, double maxDefenceStrength, Sector position)
    {
        this.maxHealth = maxHealth;
        this.maxDefenceStrength = maxDefenceStrength;
        this.position = position;

        // Set values to maximum initially
        health = maxHealth;
        defenceStrength = maxDefenceStrength;
    }


    /**
     * Calculates the current defence strength of this entity
     * @return a <code>double</code> representing the current defence strength
     */
    public abstract double getDefenceStrength();
}
