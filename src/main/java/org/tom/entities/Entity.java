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


    public double getHealth()
    {
        return health;
    }


    /**
     * Subtracts the incoming damage, reduced by the <code>defenceStrength</code> of this entity.
     * Prevents <code>health</code> from falling below 0.
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


    /**
     * Sets the health of this <code>Entity</code> from a provided <code>newHealth</code>. Prevents
     * negative health by selecting the largest value from 0 or newHealth- if newHealth is negative,
     * health is set to 0
     * @param newHealth the new health <code>double</code>
     */
    void setHealth(double newHealth)
    {
        this.health = Math.max(0.0, newHealth);
    }

    public Sector getPosition()
    {
        return position;
    }
}
