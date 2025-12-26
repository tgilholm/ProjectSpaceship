package org.tom.entities;

import org.junit.jupiter.api.*;

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
        assertEquals(10, starship.getDefenceStrength(), "Initial defence strength should be 10");

        // Do some damage to the starship
        starship.setHealth(50);
        assertEquals(5.454545454545455, starship.getDefenceStrength(), delta, "Defence strength should scale with health");

        // Remove some crew from the starship
        starship.setCrew(3);
        assertEquals(4.818181818181818, starship.getDefenceStrength(), delta, "Defence strength should scale with health and crew");
    }


    @Test
    @DisplayName("Test starship attack strength calculation")
    void getAttackStrength()
    {
        assertEquals(30, starship.getAttackStrength(), "Initial attack strength should be 30");

        // Do some damage to the starship
        starship.setHealth(50);
        assertEquals(15, starship.getAttackStrength(), delta, "Attack strength should scale with health");
    }


    @Test
    @DisplayName("takeDamage should apply max(5, attack - defence) and reduce crew accordingly")
    void takeDamage()
    {
        // Starship initial: health=100, crew=10, defence strength (computed) == 10
        // Attacker attack = 30 -> net = max(5, 30 - 10) = 20
        starship.takeDamage(30);

        assertEquals(80.0, starship.getHealth(), delta, "Health should reduce by 20");
        assertEquals(8, starship.getCrew(), "Crew should reduce by 2 (20% of 10)");
    }


    @Test
    @DisplayName("calculateCrewLost uses applied damage proportionally")
    void calculateCrewLost()
    {
        // With default crew 10, 20 damage should kill 2 crew members
        int lost = starship.calculateCrewLost(20.0);
        assertEquals(2, lost);
    }


    @Test
    @DisplayName("Starships should be unable to take actions while repairing")
    void repair()
    {
    }


    @Test
    @DisplayName("Starships can inflict damage on entities of all types")
    void attack()
    {
    }
}