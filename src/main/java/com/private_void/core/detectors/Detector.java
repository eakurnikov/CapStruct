package com.private_void.core.detectors;

import com.private_void.app.Logger;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.particles.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Detector {
    public static final double CELL_WIDTH = 10;
    public static final int CELLS_AMOUNT = 500;

    protected final CartesianPoint leftBottomPoint;
    protected final double width;

    protected ArrayList<Cell> cellsZ;
    protected ArrayList<Cell> cellsY;
    protected ArrayList<ArrayList<Cell>> cells;

    protected ArrayList<Cell> stuckCellsZ;
    protected ArrayList<Cell> stuckCellsY;
    protected ArrayList<ArrayList<Cell>> stuckCells;

    protected final double cellWidth;
    protected final int cellsAmount;

    protected int channeledAmount;
    protected int stuckAmount;
    protected int outOfDetectorAmount;
    protected int absorbedAmount;
    protected int deletedAmount;

    public Detector(final CartesianPoint center, double width) {
        this.leftBottomPoint = center.shift(0.0, -width / 2.0, -width / 2.0);
        this.width = width;

//        this.cellWidth = CELL_WIDTH;
//        this.cellsAmount = (int) (width / cellWidth);

        this.cellsAmount = CELLS_AMOUNT;
        this.cellWidth = width / cellsAmount;

        createCells();
    }

    private void init() {
        channeledAmount = 0;
        stuckAmount = 0;
        outOfDetectorAmount = 0;
        absorbedAmount = 0;
        deletedAmount = 0;
    }

    private void createCells() {
        this.cellsZ = new ArrayList<>(cellsAmount);
        this.cellsY = new ArrayList<>(cellsAmount);
        this.cells = new ArrayList<>(cellsAmount);

        this.stuckCellsZ = new ArrayList<>(cellsAmount);
        this.stuckCellsY = new ArrayList<>(cellsAmount);
        this.stuckCells = new ArrayList<>(cellsAmount);

        for (int i = 0; i < cellsAmount; i++) {
            CartesianPoint z = leftBottomPoint.shift(0.0, 0.0, i * cellWidth);
            CartesianPoint y = leftBottomPoint.shift(0.0, i * cellWidth, 0.0);

            cellsZ.add(new Cell(z));
            cellsY.add(new Cell(y));

            stuckCellsZ.add(new Cell(z));
            stuckCellsY.add(new Cell(y));

            ArrayList<Cell> yStripe = new ArrayList<>(cellsAmount);

            for (int j = 0; j < cellsAmount; j++) {
                CartesianPoint coordinate = z.shift(0.0, j * cellWidth, 0.0);
                yStripe.add(new Cell(coordinate));
            }

            cells.add(yStripe);
            stuckCells.add(yStripe);
        }
    }

    public Flux detect(Flux flux) {
        Logger.detectingParticlesStart();

        CartesianPoint point;
        init();
        List<Particle> filteredParticles = new ArrayList<>();

        for (Particle particle : flux.getParticles()) {
            if (!particle.isDeleted()) {
                if (!particle.isAbsorbed()) {

                    point = getCoordinateOnDetector(particle);
                    particle.setCoordinate(point);

                    if (isParticleWithinBorders(particle)) {
                        if (particle.isInteracted()) {
                            channeledAmount++;
                            dispatchChanneled(point);
                        } else {
                            stuckAmount++;
                            dispatchStuck(point);
                        }
                    } else {
                        outOfDetectorAmount++;
                    }

                    filteredParticles.add(particle);
                } else {
                    absorbedAmount++;
                }
            } else {
                deletedAmount++;
            }
        }

        flux.setParticles(filteredParticles);

        Logger.detectingParticlesFinish();

        Logger.totalChanneleddAmount(channeledAmount);
        Logger.totalAbsorbededAmount(absorbedAmount);
        Logger.totalStuckAmount(stuckAmount);
        Logger.totalOutOfDetector(outOfDetectorAmount);
        Logger.totalDeletedAmount(deletedAmount);

        Logger.convertingDistributionToFile();
        contvertDistributionToFile();

        return flux;
    }

    private void dispatchChanneled(final CartesianPoint point) {
        int zCellNumber = (int) ((point.getZ() - leftBottomPoint.getZ()) / cellWidth);
        int yCellNumber = (int) ((point.getY() - leftBottomPoint.getY()) / cellWidth);

        cellsZ.get(zCellNumber).register();
        cellsY.get(yCellNumber).register();
        cells.get(zCellNumber).get(yCellNumber).register();
    }

    private void dispatchStuck(final CartesianPoint point) {
        int zCellNumber = (int) ((point.getZ() - leftBottomPoint.getZ()) / cellWidth);
        int yCellNumber = (int) ((point.getY() - leftBottomPoint.getY()) / cellWidth);

        stuckCellsZ.get(zCellNumber).register();
        stuckCellsY.get(yCellNumber).register();
        stuckCells.get(zCellNumber).get(yCellNumber).register();
    }

    protected CartesianPoint getCoordinateOnDetector(final Particle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double L = leftBottomPoint.getX();

        return new CartesianPoint(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);
    }

    protected boolean isParticleWithinBorders(final Particle p) {
        return  p.getCoordinate().getY() > leftBottomPoint.getY() &&
                p.getCoordinate().getY() < leftBottomPoint.getY() + width &&
                p.getCoordinate().getZ() > leftBottomPoint.getZ() &&
                p.getCoordinate().getZ() < leftBottomPoint.getZ() + width;
    }

    public void contvertDistributionToFile() {
        convertChanneledToFile();
        convertChanneledStripeZToFile();
        convertChanneledStripeYToFile();

        convertStuckToFile();
        convertStuckStripeZToFile();
        convertStuckStripeYToFile();
    }

    public void convertChanneledToFile() {
        try (FileWriter writer = new FileWriter("channeled_ZY.txt")) {
            for (ArrayList<Cell> stripe : cells) {
                for (Cell cell : stripe) {
                    writer.write(cell.getZ() + " " + cell.getY() + " " + cell.getParticlesAmount() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertChanneledStripeZToFile() {
        try (FileWriter writer = new FileWriter("channeled_Z.txt")) {
            for (Cell cell : cellsZ) {
                writer.write(cell.getZ() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertChanneledStripeYToFile() {
        try (FileWriter writer = new FileWriter("channeled_Y.txt")) {
            for (Cell cell : cellsY) {
                writer.write(cell.getZ() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertStuckToFile() {
        try (FileWriter writer = new FileWriter("stuck_ZY.txt")) {
            for (ArrayList<Cell> stripe : stuckCells) {
                for (Cell cell : stripe) {
                    writer.write(cell.getZ() + " " + cell.getY() + " " + cell.getParticlesAmount() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertStuckStripeZToFile() {
        try (FileWriter writer = new FileWriter("stuck_Z.txt")) {
            for (Cell cell : stuckCellsZ) {
                writer.write(cell.getZ() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertStuckStripeYToFile() {
        try (FileWriter writer = new FileWriter("stuck_Y.txt")) {
            for (Cell cell : cellsZ) {
                writer.write(cell.getZ() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}