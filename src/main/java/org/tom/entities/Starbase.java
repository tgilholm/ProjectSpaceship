package org.tom.entities;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

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
     * Calculates the current defence strength of this <code>Starbase</code>
     *
     * @return a <code>double</code> representing the current defence strength
     */
    @Override
    public double getDefenceStrength()
    {
        return 0; //maxDefenceStrength * (health / maxHealth);
    }


    /**
     * Adds an incoming starship to this <code>Starbase</code> object's list of docked starships.
     * Checks if the starbase is already in the list of docked ships or is docked to any other bases.
     * @param starship a <code>Starship</code> object
     */
    public boolean dockStarship(@NonNull Starship starship)
    {
        // If the starship is not docked to this (or any other) starbase
        if (!starship.getDocked() && !(this.dockedStarships.contains(starship)))
        {
            // Add it to the list
            this.dockedStarships.add(starship);
            return true;
        }
        else
        {
            return false;
        }
    }


    public void undockStarship(Starship starship)
    {
    }


}
