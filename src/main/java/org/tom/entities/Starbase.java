package org.tom.entities;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Starbase Entity. Inherits base values <code>maxHealth</code>, <code>maxDefenceStrength</code>, <code>position</code> from
 * Entity.<br>
 * Starbases have the following pre-set values:
 * <pre>
 *       <code>maxHealth</code>, set to 500.
 *       <code>maxDefenceStrength</code>, set to 20.
 *   </pre>
 * Starbases hold a <code>List</code> of their docked Starships
 */
public class Starbase extends Entity
{
    private static final double maxDefenceStrength = 20;
    private static final double maxHealth = 500;
    private final List<Starship> dockedStarships;


    /**
     * Sets the base values for a Starbase. Passes the static final maximum values to the parent constructor
     * and initializes <code>dockedStarships</code>.
     *
     * @param position the starting position of the entity
     */
    public Starbase(Sector position)
    {
        super(maxHealth, maxDefenceStrength, position);

        // Initialise dockedStarships as empty for new bases
        dockedStarships = new ArrayList<>();
    }


    /**
     * Calculates the current defence strength of this <code>Starbase</code>. Calculates the base strength of this
     * <code>Starbase</code>. If there are any <code>Starship</code> objects docked to this, add to the total the
     * sum of their defence strengths, multiplied by the ratio of docked ships to maximum strength.
     *
     * @return a <code>double</code> representing the current defence strength
     */
    @Override
    public double getDefenceStrength()
    {
        double dockedTotal = 0;
        int dockedCount = 0;

        // Check if there are any starships docked
        if (!dockedStarships.isEmpty())
        {
            dockedTotal = getDockedShipsStrength(dockedStarships);            // Get total defence strength
            dockedCount = dockedStarships.size();
        }

        // Calculate final defence strength
        double defenceStrength = (maxDefenceStrength * (health / maxHealth)) + (dockedTotal * ((double) dockedCount / maxDefenceStrength));
        logger.debug("Defence strength of {} is {}", this, defenceStrength);
        return defenceStrength;
    }


    /**
     * Helper method to sum the defence strength of a list of <code>Starship</code> objects
     *
     * @return a <code>double</code> value of the total
     */
    double getDockedShipsStrength(@NonNull List<Starship> dockedStarships)
    {
        // Get the sum of the defence strength of each ship
        // Only adds the defence strength of ships that are non-null and not destroyed
        return dockedStarships.stream()
                .filter(s -> s != null && !s.isDestroyed())
                .map(Starship::getDefenceStrength)
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    /**
     * Adds an incoming starship to this <code>Starbase</code> object's list of docked starships.
     * Checks if the starbase is already in the list of docked ships or is docked to any other bases.
     *
     * @param starship a <code>Starship</code> object
     * @return true if docking succeeded, false otherwise
     */
    public boolean dockStarship(@NonNull Starship starship)
    {
        // Check if this starbase is destroyed
        if (destroyed)
        {
            logger.debug("{} has been destroyed. Cannot dock starships", this);
            return false;
        }

        // If the starship is part of the same fleet
        if (!Objects.equals(starship.getFleet(), this.getFleet()))
        {
            logger.debug("Cannot dock {} to {}; not in same fleet", starship, this);
            return false;
        }

        // If the starship is not destroyed
        if (starship.isDestroyed())
        {
            logger.debug("{} is destroyed, cannot dock to {}", starship, this);
            return false;
        }

        // If the starship is not docked to this (or any other) starbase
        if (!starship.getDocked() && !(this.dockedStarships.contains(starship)))
        {
            // Add it to the list
            this.dockedStarships.add(starship);
            logger.info("Docked {} to {}", starship, this);
            return true;
        } else
        {
            logger.debug("Cannot dock {} to {}", starship, this);
            return false;
        }
    }


    /**
     * Attempts to undock a <code>Starship</code> from this <code>Starbase</code>. Checks if the starship
     * is docked and appears in this starbase's list of starships.
     *
     * @param starship the <code>Starship</code> entity to undock
     * @return true if undocking succeeded, false otherwise
     */
    public boolean undockStarship(@NonNull Starship starship)
    {
        // Check if this starbase is destroyed
        if (destroyed)
        {
            logger.debug("{} has been destroyed. Cannot undock starships", this);
            return false;
        }

        // Check both objects are in the same fleet
        if (!Objects.equals(starship.getFleet(), this.getFleet()))
        {
            logger.debug("Cannot undock {} from {}; not in same fleet", starship, this);
            return false;
        }

        // If in the list, remove it
        if (starship.getDocked() && this.dockedStarships.remove(starship))
        {
            logger.info("Undocked {} from {}", starship, this);
            return true;
        } else
        {
            // If not, log it
            logger.debug("{} is not docked to {}, cannot undock", starship, this);
            return false;
        }
}


    /**
     * Exposes a package-private unmodifiable version of the <code>dockedStarships</code> list
     * @return a list of <code>Starship</code> objects
     */
    List<Starship> getDockedStarships()
    {
        return Collections.unmodifiableList(dockedStarships);
    }
}