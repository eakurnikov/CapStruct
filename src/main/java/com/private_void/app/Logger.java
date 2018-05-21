package com.private_void.app;

public class Logger {
    private static java.util.logging.Logger logger;

    private Logger() {}

    private static java.util.logging.Logger logger() {
        if (logger == null) {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
//          System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");

            logger = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());
        }
        return logger;
    }

    public static void info(String message) {
        logger().info(message);
    }

    public static void warning(String message) {
        logger().warning(message);
    }
}