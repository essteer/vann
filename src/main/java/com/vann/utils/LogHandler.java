package com.vann.utils;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

    public static void nullAttributeWarning(Class<?> clazz, UUID id, String attributeName) {
        logger.warn("{} (class: {}) {} cannot be null", id, clazz.getSimpleName(), attributeName);
    }

    public static void validIdOK(Class<?> clazz, UUID id, String classOfValidatedId) {
        logger.info("{} (class: {}) validated by {}", id, classOfValidatedId, clazz.getSimpleName());
    }

    public static void invalidIdNotFound(Class<?> clazz, UUID id, String classOfInvalidatedId) {
        logger.warn("{} (class: {}) not found by {}", id, classOfInvalidatedId, clazz.getSimpleName());
    }

    public static void validAttributeOK(Class<?> clazz, UUID id, String attributeName, String attributeValue) {
        logger.info("{} (class: {}) {} set to {}", id, clazz.getSimpleName(), attributeName, attributeValue);
    }

    public static void invalidAttributeError(Class<?> clazz, String attributeName, String attributeValue, String errorMessage) {
        logger.error("(class: {}) {} cannot be set to {}: {}", clazz.getSimpleName(), attributeName, attributeValue, errorMessage);
    }

    public static void invalidAttributeError(Class<?> clazz, UUID id, String attributeName, String attributeValue, String errorMessage) {
        logger.error("{} (class: {}) {} cannot be set to {}: {}", id, clazz.getSimpleName(), attributeName, attributeValue, errorMessage);
    }

    public static void status200OK(String requestMethod, Class<?> clazz, String callMethod) {
        logger.info("200 OK: {} request to {} via {}", requestMethod, clazz, callMethod);
    }

    public static void status201Created(String requestMethod, Class<?> clazz, String callMethod) {
        logger.info("201 Created: {} request to {} via {}", requestMethod, clazz, callMethod);
    }

    public static void status204NoContent(String requestMethod, Class<?> clazz, String callMethod) {
        logger.info("204 No Content: {} request to {} via {}", requestMethod, clazz, callMethod);
    }

    public static void status400BadRequest(String requestMethod, Class<?> clazz, String callMethod, String errorMessage) {
        logger.error("400 Bad Request: {} request to {} via {}: {}", requestMethod, clazz, callMethod, errorMessage);
    }

    public static void status404NotFound(String requestMethod, Class<?> clazz, String callMethod, String errorMessage) {
        logger.error("404 Not Found: {} request to {} via {}: {}", requestMethod, clazz, callMethod, errorMessage);
    }

    public static void status409Conflict(String requestMethod, Class<?> clazz, String callMethod, String errorMessage) {
        logger.error("409 Conflict: {} request to {} via {}: {}", requestMethod, clazz, callMethod, errorMessage);
    }

    public static void status500InternalServerError(String requestMethod, Class<?> clazz, String callMethod, String errorMessage) {
        logger.error("500 Internal Server Error: {} request to {} via {}: {}", requestMethod, clazz, callMethod, errorMessage);
    }

}
