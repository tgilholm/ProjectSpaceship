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
        Fleet fleet = new Fleet(new Player(1));
        Starbase base = new Starbase(startSector);
        base.setFleet(fleet);
        starship.setFleet(fleet);

        // Undocked repair should do nothing
        starship.setHealth(10); // below 25%
        starship.repair();
        assertEquals(10.0, starship.getHealth(), delta, "Undocked ships cannot repair");

        // Dock and repair in ticks
        starship.dockToStarbase(base);
        assertTrue(starship.getDocked(), "Ship should be docked after docking to starbase");

        starship.repair();
        assertEquals(25.0, starship.getHealth(), delta, "First repair tick should move health to 25%");

        starship.repair();
        assertEquals(50.0, starship.getHealth(), delta, "Second repair tick should move health to 50%");

        // Third tick -> 75%
        starship.repair();
        assertEquals(75.0, starship.getHealth(), delta, "Third repair tick should move health to 75%");

        starship.repair();
        assertEquals(100.0, starship.getHealth(), delta, "Final repair tick should restore to full health");
    }


    @Test
    @DisplayName("Starships can inflict damage on entities of all types")
    void attack()
    {
        Fleet fleet1 = new Fleet(new Player(1));
        Fleet fleet2 = new Fleet(new Player(2));

        starship.setFleet(fleet1);

        // Enemy target in the same sector
        Starship target = new Starship(startSector);
        target.setFleet(fleet2);

        // Basic attack: attacker attack=30, target defence 10 -> net damage = 20
        starship.attack(target);

        assertEquals(80.0, target.getHealth(), delta, "Target health should be reduced by 20");
        assertEquals(8, target.getCrew(), "Target should lose 2 crew (20% of 10)");
    }


    @Test
    @DisplayName("Test docking to starbase if part of the same fleet or not")
    void dockToStarbase()
    {
        Fleet fleetA = new Fleet(new Player(1));
        Fleet fleetB = new Fleet(new Player(2));

        Starbase base = new Starbase(startSector);
        base.setFleet(fleetA);

        // Ship in a different fleet cannot dock
        starship.setFleet(fleetB);
        starship.dockToStarbase(base);
        assertFalse(starship.getDocked(), "Ship from different fleet cannot dock");

        // Ship in the same fleet can dock
        starship.setFleet(fleetA);
        starship.dockToStarbase(base);
        assertTrue(starship.getDocked(), "Ship from same fleet should dock successfully");
    }


    @Test
    @DisplayName("Test undocking from a starbase")
    void undockFromStarbase()
    {
        Fleet fleet = new Fleet(new Player(1));
        Starbase base = new Starbase(startSector);
        base.setFleet(fleet);
        starship.setFleet(fleet);

        // Dock then undock
        starship.dockToStarbase(base);
        assertTrue(starship.getDocked(), "Ship should be docked after docking");

        starship.undockFromStarbase(base);
        assertFalse(starship.getDocked(), "Ship should be undocked after undocking");
    }
}