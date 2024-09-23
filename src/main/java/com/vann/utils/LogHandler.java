package com.vann.utils;

import java.util.UUID;

import org.apache.logging.log4j.*;


public class LogHandler {

    private static final Logger logger = LogManager.getLogger(LogHandler.class);

    public static void silentExitException() {
        logger.debug("Spring is restarting the main thread - See spring-boot-devtools");
    }

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

    public static void status200OK(String message) {
        logger.info("200 OK: {}", message);
    }

    public static void status201Created(String message) {
        logger.info("201 Created: {}", message);
    }

    public static void status204NoContent(String message) {
        logger.info("204 No Content: {}", message);
    }

    public static void status400BadRequest(String message) {
        logger.error("400 Bad Request: {}", message);
    }

    public static void status404NotFound(String message) {
        logger.error("404 Not Found: {}", message);
    }

    public static void status409Conflict(String message) {
        logger.error("409 Conflict: {}", message);
    }

    public static void status500InternalServerError(String message) {
        logger.error("500 Internal Server Error: {}", message);
    }

}
