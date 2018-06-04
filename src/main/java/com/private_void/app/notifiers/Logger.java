package com.private_void.app.notifiers;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Logger {
    private static java.util.logging.Logger logger;
    private static TextArea outputResource;

    private Logger() {}

    private static java.util.logging.Logger logger() {
        if (logger == null) {
//          System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
//          System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-7s] %5$s %n");

            logger = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());

            if (outputResource != null) {
                Formatter formatter = new SimpleFormatter();
                logger.setUseParentHandlers(false);
                logger.addHandler(new Handler() {
                    @Override
                    public void publish(LogRecord record) {
                        Platform.runLater(() -> outputResource.appendText(formatter.format(record)));
                    }

                    @Override
                    public void flush() {
                    }

                    @Override
                    public void close() {
                    }
                });
            }
        }

        return logger;
    }

    public static void setOutputResource(TextArea textArea) {
        outputResource = textArea;
    }

    public static void info(String message) {
        logger().info(message);
    }

    public static void warning(String message) {
        logger().warning(message);
    }
}