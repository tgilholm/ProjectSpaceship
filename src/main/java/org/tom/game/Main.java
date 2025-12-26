package org.tom.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tom.entities.*;


public class Main
{

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

        /*
        Gets the first starbase in fleet2. If it exists (ifPresent), get the first two starships and if they
        exist, dock them to the starbase.
         */
        fleet2.getStarbaseAt(0).ifPresent(starbase ->
        {
            fleet2.getStarshipAt(0).ifPresent(s1 -> fleet2.dockStarshipsTo(starbase, s1));
            fleet2.getStarshipAt(1).ifPresent(s2 -> fleet2.dockStarshipsTo(starbase, s2));
        });

        //Get the first ship from fleet1. If it exists, attack the remaining undocked starship in fleet2 twice
        fleet1.getStarshipAt(0).ifPresent(starship ->
                fleet2.getStarshipAt(2).ifPresent(enemy ->
                {
                    starship.attack(enemy);
                    starship.attack(enemy);
                }));

        //Dock the undocked fleet2 starship to the starbase and set it to repair
        fleet2.getStarshipAt(2).ifPresent(starship ->
                fleet2.getStarbaseAt(0).ifPresent(starbase -> {
                    starship.dockToStarbase(starbase);
                    starship.repair();  // todo implement repair
                }));

        // Command fleet1 to attack fleet2's starbase until it is destroyed
        fleet2.getStarbaseAt(0).ifPresent(starbase -> {
            while (starbase.getHealth() > 0)
            {
                fleet1.attackWithAll(starbase);
            }
        });
    }
}