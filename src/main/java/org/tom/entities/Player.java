package org.tom.entities;

import jdk.jfr.Unsigned;

/**
 * The <code>Player</code> record acts as a wrapper around a <code>int</code> player number.
 * This allows future extensibility with wins, high scores, etc.
 * @param playerNo the unique player number of this object
 */
public record Player(@Unsigned int playerNo)
{}
