package org.tom.entities;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * and initialises <code>dockedStarships</code>.
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
        return (maxDefenceStrength * (health / maxHealth)) + (dockedTotal * (dockedCount / maxDefenceStrength));
    }


    /**
     * Helper method to sum the defence strength of a list of <code>Starship</code> objects
     * @return a <code>double</code> value of the total
     */
    private double getDockedShipsStrength(@NonNull List<Starship> dockedStarships)
    {
        // Get the sum of the defence strength of each ship
        return dockedStarships.stream()
                .map(Starship::getDefenceStrength).mapToDouble(Double::doubleValue).sum();
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
        // If the starship is not docked to this (or any other) starbase
        if (!starship.getDocked() && !(this.dockedStarships.contains(starship)))
        {
            // Add it to the list
            this.dockedStarships.add(starship);
            logger.info("Docked starship: {} to starbase: {}", starship, this);
            return true;
        } else
        {
            logger.info("Cannot dock starship: {} to starbase: {}", starship, this);
            return false;
        }
    }


    /**
     * Attempts to undock a <code>Starship</code> from this <code>Starbase</code>. Checks if the starship
     * is docked and appears in this starbase's list of starships.
     * @param starship the <code>Starship</code> entity to undock
     * @return true if undocking succeeded, false otherwise
     */
    public boolean undockStarship(@NonNull Starship starship)
    {
        // If in the list, remove it
        if (starship.getDocked() && this.dockedStarships.remove(starship))
        {
            logger.info("Undocked starship: {} from starbase: {}", starship, this);
            return true;
        } else
        {
            // If not, log it
            logger.info("Starship: {} is not docked to starbase: {}, cannot undock", starship, this);
            return false;
        }
    }




}
