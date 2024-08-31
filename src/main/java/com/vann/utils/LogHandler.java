package com.vann.utils;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LogHandler {

    private static final Logger logger = LogManager.getLogger(LogHandler.class);

    public static void applicationStartupError(String errorMessage) {
        logger.error("Unexpected error during application startup: {}", errorMessage);
    }

    public static void serviceClassInitOK(String className) {
        logger.info("{} init OK", className);
    }

    public static void serviceClassInitError(String errorMessage) {
        logger.error("Service class init error: {}", errorMessage);
    }

    public static void createInstanceOK(Class<?> clazz, UUID id, Object... args) {
        String argsString = getArgsString(args);
        logger.info("{} (class: {}) created with args: [{}]", id, clazz.getSimpleName(), argsString);
    }

    public static void createInstanceError(Class<?> clazz, Object... args) {
        String argsString = getArgsString(args);
        logger.error("Failed to create {} instance with args: [{}]", clazz.getSimpleName(), argsString);
    }

    private static String getArgsString(Object... args) {
        StringBuilder argsString = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                argsString.append(arg.toString()).append(", ");
            } else {
                argsString.append("null, ");
            }
        }
        if (argsString.length() > 0) {
            argsString.setLength(argsString.length() - 2);
        }
        return argsString.toString();
    }

    public static void nullAttributeWarning(Class<?> clazz, UUID id, String attributeName) {
        logger.warn("{} (class: {}) {} cannot be null", id, clazz.getSimpleName(), attributeName);
    }

    public static void validAttributeOK(Class<?> clazz, UUID id, String attributeName, String attributeValue) {
        logger.info("{} (class: {}) {} set to {}", id, clazz.getSimpleName(), attributeName, attributeValue);
    }

    public static void invalidAttributeError(Class<?> clazz, UUID id, String attributeName, String attributeValue, String errorMessage) {
        logger.error("{} (class: {}) {} cannot be set to {}: {}", id, clazz.getSimpleName(), attributeName, attributeValue, errorMessage);
    }

    
	
}
