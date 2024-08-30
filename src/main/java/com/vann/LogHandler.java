package com.vann;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHandler {

    private static final Logger logger = LogManager.getLogger("com.vann");

    public static void serviceClassInitOK(String className) {
        logger.info("{} init OK", className);
    }

    public static void serviceClassInitError(String errorMessage) {
        logger.fatal("Service class init error: {}", errorMessage);
    }
	
}
