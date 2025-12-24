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


    /**
     * Creates a new list of starships in the start sector
     */
    @BeforeEach
    void setStarships()
    {
        // Add some example starships
        starships = List.of(new Starship[]{new Starship(sector), new Starship(sector), new Starship(sector)});
    }


    @BeforeEach
    void setStarbase()
    {
        starbase = new Starbase(sector);
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
}