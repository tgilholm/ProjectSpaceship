package org.tom.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Import static assert methods for readability
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Starship Tests")
class StarshipTest
{
    private Starship starship;
    private static Sector startSector;
    public static final double delta = 1e-9;        // Exact equality between doubles is impractical, use a delta of .000000001


    /**
     * Define the static startSector for instantiating new <code>Starship</code> objects
     */
    @BeforeAll
    static void setStartSector()
    {
        startSector = new Sector(0, 0);
    }


    /**
     * Create a new <code>Starship</code>. Annotated <code>@BeforeEach</code>
     * so that each method has a fresh <code>Starship</code> to work with
     */
    @BeforeEach
    void setStarship()
    {
        starship = new Starship(startSector);
    }


    @Test
    @DisplayName("Test starship defence strength calculation")
    void getDefenceStrength()
    {

        assertEquals(10, starship.getDefenceStrength(),"Initial defence strength should be 10");

        // Do some damage to the starship
        starship.setHealth(50);
        assertEquals(5.454545454545455, starship.getDefenceStrength(), delta, "Defence strength should scale with health");

        // Remove some crew from the starship
        starship.setCrew(3);
        assertEquals(4.818181818181818, starship.getDefenceStrength(), delta, "Defence strength should scale with health and crew");
    }
}