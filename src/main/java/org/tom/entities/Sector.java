package org.tom.entities;

/**
 * Defines a location on the map grid with an x and y coordinate.
 */
public class Sector
{
    private final int x;
    private final int y;

    public Sector(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
