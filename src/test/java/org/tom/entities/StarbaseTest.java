package org.tom.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Starbase Tests")
class StarbaseTest
{
    private List<Starship> starships;
    private static Sector sector;
    private static Fleet fleet;
    private Starbase starbase;
    public static final double delta = 1e-9;


    /**
     * Set the sector to 0,0 for all new entities
     */
    @BeforeAll
    static void setSector()
    {
        sector = new Sector(0, 0);
    }

    @BeforeAll
    static void setFleet()
    {
        fleet = new Fleet(new Player(1));
    }

    /**
     * Creates a new list of starships in the start sector
     */
    @BeforeEach
    void setStarships()
    {
        // Add some example starships and set them to a fleet
        starships = List.of(new Starship[]{new Starship(sector), new Starship(sector), new Starship(sector)});

        for (Starship s : starships)
        {
            s.setFleet(fleet);
        }
    }


    /**
     * Creates a new starbase and sets the fleet
     */
    @BeforeEach
    void setStarbase()
    {
        starbase = new Starbase(sector);
        starbase.setFleet(fleet);
    }


    @Test
    @DisplayName("Test docked starship defence strength total calculation")
    void getDockedShipsStrength()
    {
        // Dock the starships to the starbase
        for (Starship s : starships)
        {
            starbase.dockStarship(s);
        }

        // Test calculation
        // New Starships have defence strength 10
        double expectedDockedTotal = 30.0;
        assertEquals(expectedDockedTotal, starbase.getDockedShipsStrength(starships), delta,
                "Total defence strength of docked ships should be 30.0");
    }


    @Test
    @DisplayName("Test Starbase defence strength calculation")
    void getDefenceStrength()
    {
        // Dock the starships to the starbase
        for (Starship s : starships)
        {
            starbase.dockStarship(s);
        }


        /*
        Starbase base defence: 20 * (health/maxHealth) = 20 * (500/500) = 20.0
        Docked ships: dockedTotal * (dockedCount / maxDefenceStrength)
        dockedTotal = 30.0, dockedCount = 3, maxDefenceStrength = 20 => 30 * (3/20) = 4.5
        Total expected = 20.0 + 4.5 = 24.5
         */
        double expectedStarbaseDefence = 24.5;
        assertEquals(expectedStarbaseDefence, starbase.getDefenceStrength(), delta,
                "Starbase defence strength should be 24.5 when 3 fresh starships are docked");
    }


    @Test
    @DisplayName("Test docking in-fleet, out-fleet and destroyed ships")
    void dockStarship()
    {
        // Dock in-fleet ships
        for (Starship s : starships)
        {
            assertTrue(starbase.dockStarship(s), "Should dock ship belonging to same fleet");
        }

        // Out-of-fleet ship should not dock
        Fleet otherFleet = new Fleet(new Player(2));
        Starship outsider = new Starship(sector);
        outsider.setFleet(otherFleet);
        assertFalse(starbase.dockStarship(outsider), "Ship from another fleet should not be able to dock");

        // Destroyed ship should not dock
        Starship dead = new Starship(sector);
        dead.setFleet(fleet);
        dead.takeDamage(1000); // ensure destroyed
        assertTrue(dead.isDestroyed(), "Ship should be destroyed");
        assertFalse(starbase.dockStarship(dead), "Destroyed ship should not be allowed to dock");
    }


    @Test
    @DisplayName("Test undocking in-fleet ships")
    void undockStarship()
    {
        // Dock all ships
        for (Starship s : starships)
        {
            s.dockToStarbase(starbase);
        }

        assertEquals(3, starbase.getDockedStarships().size(), "Three ships are docked");

        // Undock one
        boolean removed = starbase.undockStarship(starships.getFirst());
        assertTrue(removed, "Undocking should succeed for docked ship");
        assertEquals(2, starbase.getDockedStarships().size(), "Two ships should remain docked");

        // Undocking a non-docked ship should fail
        Starship notDocked = new Starship(sector);
        notDocked.setFleet(fleet);
        assertFalse(starbase.undockStarship(notDocked), "Cannot undock a ship that is not docked");
    }
}