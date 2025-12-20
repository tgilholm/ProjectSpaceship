package org.tom.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main
{
    private final static Logger logger = LogManager.getLogger();


    static void main(String[] args)
    {
        System.out.print("Hello World!");
        logger.info("hello");
    }
}