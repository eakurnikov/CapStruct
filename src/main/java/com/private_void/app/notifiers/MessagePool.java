package com.private_void.app.notifiers;

public class MessagePool {
    private static long creatingFluxTimer;
    private static long creatingCapillarsTimer;
    private static long processingParticlesTimer;
    private static long detectingParticlesTimer;
    private static long renderingTimer;

    public static String fluxCreationStart() {
        creatingFluxTimer = System.nanoTime();
        return "Creating flux start ...";
    }

    public static String fluxCreationFinish() {
        String message = "Creating flux finish. Total time = "
                + (System.nanoTime() - creatingFluxTimer) / 1_000_000 + " ms\n";
        creatingFluxTimer = 0L;
        return message;
    }

    public static String creatingCapillarsStart() {
        creatingCapillarsTimer = System.nanoTime();
        return "Creating capillars start ...";
    }

    public static String capillarsDensityTooBig(double maxDensity) {
        return "Capillars density is too big, it has been automatically set to " + maxDensity;
    }

    public static String createdCapillarsPercent(int percent) {
        return "    ... " + percent + "% capillars created";
    }

    public static String creatingCapillarsFinish() {
        String message = "Creating capillars finish. Total time = "
                + (System.nanoTime() - creatingCapillarsTimer) / 1_000_000 + " ms\n";
        creatingCapillarsTimer = 0L;
        return message;
    }

    public static String interactionStart() {
        processingParticlesTimer = System.nanoTime();
        return "Particles-capillars interaction start ...";
    }

    public static String particleDeleted() {
        return "Finding the point of entry of the particle into the capillary failed. Particle has been deleted.";
    }

    public static String processedParticlesPercent(int percent) {
        return "    ... " + percent + "% paricles processed";
    }

    public static String processedCapillarsPercent(int percent) {
        return "    ... " + percent + "% capillars processed";
    }

    public static String interactionFinish() {
        String message = "Particles-capillars interaction finish. Total time = "
                + (System.nanoTime() - processingParticlesTimer) / 1_000_000 + " ms\n";
        processingParticlesTimer = 0L;
        return message;
    }

    public static String detectingParticlesStart() {
        detectingParticlesTimer = System.nanoTime();
        return "Detecting particles start ...";
    }

    public static String detectingParticlesFinish() {
        String message = "Detecting particles finish. Total time = "
                + (System.nanoTime() - detectingParticlesTimer) / 1_000_000 + " ms\n";
        detectingParticlesTimer = 0L;
        return message;
    }

    public static String renderingStart() {
        renderingTimer = System.nanoTime();
        return "Rendering start ...";
    }

    public static String renderingFinish() {
        String message = "Rendering finish. Total time = "
                + (System.nanoTime() - renderingTimer) / 1_000_000 + " ms\n";
        renderingTimer = 0L;
        return message;
    }

    public static String totalChanneleddAmount(int amount)  {
        return "TOTAL PARTICLES CHANNELED = " + amount;
    }

    public static String totalAbsorbededAmount(int amount) {
        return "TOTAL ABSORBED PARTICLES = " + amount;
    }

    public static String totalPiercedAmount(int amount) {
        return "TOTAL PARTICLES PIERCED= " + amount;
    }

    public static String totalOutOfDetector(int amount) {
        return "TOTAL PARTICLES OUT OF DETECTOR = " + amount;
    }

    public static String totalDeletedAmount(int amount) {
        return "TOTAL DELETED PARTICLES = " + amount;
    }

    public static String averageExpansionAngle(double angle) {
        return "AVERAGE EXPANSION ANGLE = " + angle;
    }

    public static String standardAngleDeviation(double deviation) {
        return "STANDARD ANGLE DEVIATION = " + deviation;
    }

    public static String convertingDistributionToFile() {
        return "Converting distribution to file ...\n";
    }
}