package org.tom.entities;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * The Starship Entity inherits base values <code>maxHealth</code>, <code>maxDefenceStrength</code>, <code>position</code> from
 * Entity. <br>Starships have the following pre-set values:
 * <pre>
 *     <code>maxAttackStrength</code>, set to 30.
 *     <code>maxDefenceStrength</code>, set to 10.
 *     <code>maxHealth</code>, set to 100.
 *     <code>maxCrew</code>, set to 10.
 * </pre>
 *
 */
public class Starship extends Entity
{
    // All Starship entities share the same base values
    private static final double maxAttackStrength = 30;
    private static final double maxDefenceStrength = 10;
    private static final double maxHealth = 100;
    private static final int maxCrew = 10;

    private boolean docked;
    private boolean repairing;
    private int crew;
    private Starbase starbase;


    /**
     * Sets the base values for a Starship. Passes the static final maximum values to the parent constructor and sets
     * <code>attackStrength</code>, <code>crew</code> to their maximums
     *
     * @param position the starting position of the entity
     */
    public Starship(Sector position)
    {
        super(maxHealth, maxDefenceStrength, position);

        crew = maxCrew;         // Maximum crew for new ships
        docked = false;         // Undocked by default
        repairing = false;      // Not repairing by default
    }


    /**
     * Calculates the current defence strength of this <code>Starship</code>
     * based on its current, and maximum crew and health
     *
     * @return a <code>double</code> representing the current defence strength
     */
    @Override
    public double getDefenceStrength()
    {
        return maxDefenceStrength * ((health + crew) / (maxHealth + maxCrew));
    }


    /**
     * Calculates the current attack strength of this <code>Starship</code>
     * based on its maximum attack strength and the ratio between its current and
     * maximum health
     *
     * @return a <code>double</code> representing the current attack strength
     */
    public double getAttackStrength()
    {
        return maxAttackStrength * (health / maxHealth);
    }


    /**
     * Sets crew to a specified <code>newCrew</code> amount. Ensures that the crew count
     * never falls below 1 using <code>Math.max</code>
     * <p>
     * Note: is package-private (no keyword) to allow access to test methods
     * </p>
     *
     * @param newCrew the new number of crew
     */
    void setCrew(int newCrew)
    {
        // If newCrew < 1, set crew to 1 instead
        this.crew = Math.max(1, newCrew);
    }


    /**
     * Moves this <code>Starship</code> to the specified <code>Sector</code>
     * <p>
     * Note: this method is not part of the parent class <code>Entity</code>;
     * not all entities are able to move</p>
     *
     * @param newSector the new <code>Sector</code> object
     */
    void setSector(@NotNull Sector newSector)
    {
        logger.info("Moving starship {} from sector {} to {}", this, getSector(), newSector);
        this.sector = newSector;
    }


    /**
     * Gets the <code>docked</code> boolean from this <code>Starship</code>
     *
     * @return true if the ship is docked, false otherwise
     */
    public boolean getDocked()
    {
        return this.docked;
    }


    /**
     * Docks this <code>Starship</code> to the provided <code>Starbase</code>
     *
     * @param starbase the <code>Starbase</code> to dock to
     */
    public void dockToStarbase(@NotNull Starbase starbase)
    {
        starbase.dockStarship(this);
        this.docked = true;
        logger.info("Docked starship {} to starbase {}", this, starbase);
    }


    /**
     * Undocks this <code>Starship</code> from the specified <code>Starbase</code>
     *
     * @param starbase the <code>Starbase</code> to undock from
     */
    public void undockFromStarbase(@NotNull Starbase starbase)
    {
        starbase.undockStarship(this);
        this.docked = false;
        logger.info("Undocked starship {} from starbase {}", this, starbase);
    }


    public void repair()
    {

    }


    /**
     * Attacks an <code>Entity</code> with the current attack strength of this <code>Starship</code>
     * @param target the <code>Entity</code> to attack
     */
    public void attack(@NonNull Entity target)
    {
        target.takeDamage(getAttackStrength());
    }


    /**
     * Overrides the <code>takeDamage</code> method from <code>Entity</code>.
     * Executes the base method and then removes crew
     * @param damage the incoming damage, as a <code>double</code>
     */
    @Override
    public void takeDamage(double damage)
    {
        super.takeDamage(damage);
        
        // Remove crew
        setCrew(crew - calculateCrewLost(damage));
    }


    /**
     * Helper method to calculate the amount of crew lost based on the current crew and the
     * ratio between the attack damage and the max health of this <code>Starship</code>
     * @param damage the incoming damage, as a <code>double</code>
     * @return the amount of crew lost, as an <code>int</code>
     */
    int calculateCrewLost(double damage)
    {
        return Math.round((float) (damage / maxHealth) * crew);
    }

}