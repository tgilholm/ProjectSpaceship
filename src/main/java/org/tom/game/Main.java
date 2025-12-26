package org.tom.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tom.entities.*;

import java.util.List;

public class Main
{
    private final static Logger logger = LogManager.getLogger();


    /**
     * Executes the demo sequence
     *
     * @param args start arguments
     */
    static void main(String[] args)
    {
        System.out.println("Creating fleets");

        // Create players
        Player player1 = new Player(1);
        Player player2 = new Player(2);

        // Create sectors
        Sector sector1 = new Sector(1, 1);
        Sector sector2 = new Sector(2, 2);

        // Create player 1 fleet with 1 starbase and 3 starships in sector 1
        Fleet fleet1 = new Fleet(player1);
        fleet1.addEntities(new Starbase(sector1), new Starship(sector1), new Starship(sector1), new Starship(sector1));

        // Create player 2 fleet with 1 starbase and 3 starships in sector 2
        Fleet fleet2 = new Fleet(player2);
        fleet2.addEntities(new Starbase(sector2), new Starship(sector2), new Starship(sector2), new Starship(sector2));

        // Move all player 1 ships to sector 2
        fleet1.moveAllEntities(sector2);

        // Dock two ships from the player 2 fleet to the starbase

    }
}