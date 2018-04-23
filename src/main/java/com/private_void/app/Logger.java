package com.private_void.app;

public class Logger {
    private static java.util.logging.Logger logger;

    private static long creatingFluxTimer;
    private static long creatingCapillarsTimer;
    private static long processingParticlesTimer;
    private static long detectingParticlesTimer;
    private static long renderingTimer;

    private Logger() {}

    private static java.util.logging.Logger log() {
        if (logger == null) {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
//          System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s [%1$tc]%n");

            logger = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());
        }
        return logger;
    }

    public static void fluxCreationStart() {
        creatingFluxTimer = System.nanoTime();
        log().info("Creating flux start ...");
    }

    public static void fluxCreationFinish() {
        log().info("Creating flux finish. Total time = "
                + (System.nanoTime() - creatingFluxTimer) / 1_000_000 + " ms\n");
        creatingFluxTimer = 0L;
    }

    public static void creatingCapillarsStart() {
        creatingCapillarsTimer = System.nanoTime();
        log().info("Creating capillars start ...");
    }

    public static void capillarsDensityTooBig(double maxDensity) {
        log().warning("Capillars density is too big, it has been automatically set to " + maxDensity);
    }

    public static void createdCapillarsPercent(int percent) {
        log().info("    ... " + percent + "% capillars created");
    }

    public static void creatingCapillarsFinish() {
        log().info("Creating capillars finish. Total time = "
                + (System.nanoTime() - creatingCapillarsTimer) / 1_000_000 + " ms\n");
        creatingCapillarsTimer = 0L;
    }

    public static void interactionStart() {
        processingParticlesTimer = System.nanoTime();
        log().info("Particles-capillars interaction start ...");
    }

    public static void particleDeleted() {
        log().warning("Finding the point of entry of the particle into the capillary failed. Particle has been deleted.");
    }

    public static void processedParticlesPercent(int percent) {
        log().info("    ... " + percent + "% paricles processed");
    }

    public static void processedCapillarsPercent(int percent) {
        log().info("    ... " + percent + "% capillars processed");
    }

    public static void interactionFinish() {
        log().info("Particles-capillars interaction finish. Total time = "
                + (System.nanoTime() - processingParticlesTimer) / 1_000_000 + " ms\n");
        processingParticlesTimer = 0L;
    }

    public static void detectingParticlesStart() {
        detectingParticlesTimer = System.nanoTime();
        log().info("Detecting particles start ...");
    }

    public static void detectingParticlesFinish() {
        log().info("Detecting particles finish. Total time = "
                + (System.nanoTime() - detectingParticlesTimer) / 1_000_000 + " ms\n");
        detectingParticlesTimer = 0L;
    }

    public static void renderingStart() {
        renderingTimer = System.nanoTime();
        log().info("Rendering start ...");
    }

    public static void renderingFinish() {
        log().info("Rendering finish. Total time = "
                + (System.nanoTime() - renderingTimer) / 1_000_000 + " ms\n");
        renderingTimer = 0L;
    }

    public static void totalChanneleddAmount(int amount)  {
        log().info("TOTAL PARTICLES CHANNELED = " + amount);
    }

    public static void totalAbsorbededAmount(int amount) {
        log().info("TOTAL ABSORBED PARTICLES = " + amount);
    }

    public static void totalPiercedAmount(int amount) {
        log().info("TOTAL PARTICLES PIERCED= " + amount);
    }

    public static void totalOutOfDetector(int amount) {
        log().info("TOTAL PARTICLES OUT OF DETECTOR = " + amount);
    }

    public static void totalDeletedAmount(int amount) {
        log().info("TOTAL DELETED PARTICLES = " + amount);
    }

    public static void convertingDistributionToFile() {
        log().info("Converting distribution to file ...\n");
    }
}