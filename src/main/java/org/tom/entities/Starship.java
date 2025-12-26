package org.tom.entities;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

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
    void setSector(@NonNull Sector newSector)
    {
        // Only allow movement if not destroyed
        if (this.destroyed)
        {
            logger.debug("{} has been destroyed and cannot move", this);
            return;
        }

        // Only allow movement if undocked
        if (this.docked)
        {
            // Check if repairing
            if (this.repairing)
            {
                // Skip the move and repair instead
                repair();
                logger.debug("{} is repairing. Skipping move.", this);
            } else
            {
                logger.debug("{} is docked and cannot move", this);
            }
        }

        // Make the move if all checks succeeded
        logger.info("Moving {} from sector: {} to: {}", this, getSector(), newSector);
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
     * Get the number of crew aboard this <code>Starship</code>
     *
     * @return an <code>int</code> for the number of crew
     */
    int getCrew()
    {
        return crew;
    }


    /**
     * Docks this <code>Starship</code> to the provided <code>Starbase</code>
     *
     * @param starbase the <code>Starbase</code> to dock to
     */
    public void dockToStarbase(@NonNull Starbase starbase)
    {
        if (this.destroyed)
        {
            logger.debug("{} has been destroyed and cannot dock", this);
            return;
        }

        if (starbase.dockStarship(this))
        {
            this.docked = true;
        }
    }


    /**
     * Undocks this <code>Starship</code> from the specified <code>Starbase</code>
     *
     * @param starbase the <code>Starbase</code> to undock from
     */
    public void undockFromStarbase(@NonNull Starbase starbase)
    {
        if (this.destroyed)
        {
            logger.debug("{} has been destroyed and cannot undock", this);
            return;
        }

        if (starbase.undockStarship(this))
        {
            // Undock the ship
            this.docked = false;
        }
    }


    public void repair()
    {
        // Check if destroyed
        if (destroyed)
        {
            logger.debug("{} has been destroyed and cannot repair", this);
            return;
        }

        // Check if docked
        if (!docked)
        {
            logger.debug("Cannot repair, {} is undocked", this);
            return;
        }


        // Repair if all checks succeeded
        repairing = true;

        // Below 25%
        if (health < maxHealth * 0.25)
        {
            this.setHealth(maxHealth * 0.25);
            logger.debug("Set health of {} to 25%", this);
        } else if (health >= maxHealth * 0.25 && health < maxHealth * 0.5)  // Between 25% and 50%
        {
            this.setHealth(maxHealth * 0.5);
            logger.debug("Set health of {} to 50%", this);
        } else if (health >= maxHealth * 0.5 && health < maxHealth * 0.75)    // Between 50% and 75%
        {
            this.setHealth(maxHealth * 0.75);
            logger.debug("Set health of {} to 75%", this);
        } else
        {
            // Above 75%, just repair fully & stop repairing
            this.setHealth(maxHealth);
            logger.debug("Set health of {} to 100%", this);
            repairing = false;
        }
    }


    /**
     * Attacks an <code>Entity</code> with the current attack strength of this <code>Starship</code>.
     * Attacking is only permitted if the target entity is in the enemy fleet and in the same sector,
     * and this <code>Starship</code> is not docked
     *
     * @param target the <code>Entity</code> to attack
     */
    public void attack(@NonNull Entity target)
    {
        // Check if destroyed
        if (destroyed)
        {
            logger.debug("{} has been destroyed and cannot attack", this);
            return;
        }

        // Check if undocked
        if (this.docked)
        {
            // Check if repairing
            if (this.repairing)
            {
                // If so, skip the move and repair instead
                repair();
                logger.debug("{} is repairing. Skipping attack move.", this);
            } else
            {
                logger.debug("{} is docked and cannot attack", this);
            }
        }

        // Check if both entities are in the same sector
        if (!target.getSector().equals(this.sector))
        {
            logger.debug("{} cannot attack entity: {} - they are not in the same sector", this, target);
            return;
        }

        // Check if the target is in the same fleet
        if (!Objects.equals(target.getFleet(), this.getFleet()))
        {
            logger.info("{} attacking entity: {}", this, target);
            target.takeDamage(getAttackStrength());
        } else
        {
            logger.debug("{} cannot attack {}, they are in the same fleet", this, target);
        }
    }


    /**
     * Overrides the <code>takeDamage</code> method from <code>Entity</code>.
     * Executes the base method and then removes crew
     *
     * @param damage the incoming damage, as a <code>double</code>
     */
    @Override
    public void takeDamage(double damage)
    {
        super.takeDamage(damage);
        double appliedDamage = Math.min(Math.max(5, damage - defenceStrength), this.health);

        // Remove crew based on applied damage, not total damage
        int crewLost = calculateCrewLost(appliedDamage);
        setCrew(crew - crewLost);
        logger.debug("{} lost {} crew, remaining crew: {}", this, crewLost, crew);
    }


    /**
     * Helper method to calculate the amount of crew lost based on the current crew and the
     * ratio between the attack damage and the max health of this <code>Starship</code>
     *
     * @param damage the incoming damage, as a <code>double</code>
     * @return the amount of crew lost, as an <code>int</code>
     */
    int calculateCrewLost(double damage)
    {
        return Math.round((float) (damage / maxHealth) * crew);
    }

}