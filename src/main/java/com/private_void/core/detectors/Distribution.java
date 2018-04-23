package com.private_void.core.detectors;



import com.private_void.core.geometry.space_2D.CartesianPoint2D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Distribution {
    private final ArrayList<CartesianPoint2D> channeledImage;
    private final ArrayList<CartesianPoint2D> piercedImage;

    private final ArrayList<ArrayList<Cell>> channeledCells;
    private final ArrayList<ArrayList<Cell>> piercedCells;

    private final ArrayList<Cell> channeledCellsX;
    private final ArrayList<Cell> piercedCellsX;

    private final ArrayList<Cell> channeledCellsY;
    private final ArrayList<Cell> piercedCellsY;

    private final int channeledAmount;
    private final int piercedAmount;

    private final int outOfDetectorAmount;
    private final int absorbedAmount;
    private final int deletedAmount;

    private Distribution(final ArrayList<CartesianPoint2D> channeledImage,
                         final ArrayList<CartesianPoint2D> piercedImage,

                         final ArrayList<ArrayList<Cell>> channeledCells,
                         final ArrayList<ArrayList<Cell>> piercedCells,

                         final ArrayList<Cell> channeledCellsX,
                         final ArrayList<Cell> piercedCellsX,

                         final ArrayList<Cell> channeledCellsY,
                         final ArrayList<Cell> piercedCellsY,

                         int channeledAmount,
                         int piercedAmount,

                         int outOfDetectorAmount,
                         int absorbedAmount,
                         int deletedAmount) {

        this.channeledImage = channeledImage;
        this.piercedImage = piercedImage;

        this.channeledCells = channeledCells;
        this.piercedCells = piercedCells;

        this.channeledCellsX = channeledCellsX;
        this.piercedCellsX = piercedCellsX;

        this.channeledCellsY = channeledCellsY;
        this.piercedCellsY = piercedCellsY;

        this.channeledAmount = channeledAmount;
        this.piercedAmount = piercedAmount;

        this.outOfDetectorAmount = outOfDetectorAmount;
        this.absorbedAmount = absorbedAmount;
        this.deletedAmount = deletedAmount;
    }

    public ArrayList<CartesianPoint2D> getChanneledImage() {
        return channeledImage;
    }

    public ArrayList<CartesianPoint2D> getPiercedImage() {
        return piercedImage;
    }

    public ArrayList<ArrayList<Cell>> getChanneledCells() {
        return channeledCells;
    }

    public ArrayList<ArrayList<Cell>> getPiercedCells() {
        return piercedCells;
    }

    public ArrayList<Cell> getChanneledCellsX() {
        return channeledCellsX;
    }

    public ArrayList<Cell> getPiercedCellsX() {
        return piercedCellsX;
    }

    public ArrayList<Cell> getChanneledCellsY() {
        return channeledCellsY;
    }

    public ArrayList<Cell> getPiercedCellsY() {
        return piercedCellsY;
    }

    public int getChanneledAmount() {
        return channeledAmount;
    }

    public int getPiercedAmount() {
        return piercedAmount;
    }

    public int getOutOfDetectorAmount() {
        return outOfDetectorAmount;
    }

    public int getAbsorbedAmount() {
        return absorbedAmount;
    }

    public int getDeletedAmount() {
        return deletedAmount;
    }

    public void contvertToFile() {
        convertChanneledToFile();
        convertPiercedToFile();

        convertChanneledStripeXToFile();
        convertPiercedStripeXToFile();

        convertChanneledStripeYToFile();
        convertPiercedStripeYToFile();
    }

    public void convertChanneledToFile() {
        try (FileWriter writer = new FileWriter("channeled_XY.txt")) {
            for (ArrayList<Cell> stripe : channeledCells) {
                for (Cell cell : stripe) {
                    writer.write(cell.getX() + " " + cell.getY() + " " + cell.getParticlesAmount() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertPiercedToFile() {
        try (FileWriter writer = new FileWriter("pierced_XY.txt")) {
            for (ArrayList<Cell> stripe : piercedCells) {
                for (Cell cell : stripe) {
                    writer.write(cell.getX() + " " + cell.getY() + " " + cell.getParticlesAmount() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertChanneledStripeXToFile() {
        try (FileWriter writer = new FileWriter("channeled_X.txt")) {
            for (Cell cell : channeledCellsX) {
                writer.write(cell.getX() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertPiercedStripeXToFile() {
        try (FileWriter writer = new FileWriter("pierced_X.txt")) {
            for (Cell cell : piercedCellsX) {
                writer.write(cell.getX() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertChanneledStripeYToFile() {
        try (FileWriter writer = new FileWriter("channeled_Y.txt")) {
            for (Cell cell : channeledCellsY) {
                writer.write(cell.getX() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertPiercedStripeYToFile() {
        try (FileWriter writer = new FileWriter("pierced_Y.txt")) {
            for (Cell cell : channeledCellsY) {
                writer.write(cell.getX() + " " + cell.getParticlesAmount() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ArrayList<CartesianPoint2D> channeledImage;
        private ArrayList<CartesianPoint2D> piercedImage;

        private ArrayList<ArrayList<Cell>> channeledCells;
        private ArrayList<ArrayList<Cell>> piercedCells;

        private ArrayList<Cell> channeledCellsX;
        private ArrayList<Cell> piercedCellsX;

        private ArrayList<Cell> channeledCellsY;
        private ArrayList<Cell> piercedCellsY;

        private int channeledAmount;
        private int piercedAmount;

        private int outOfDetectorAmount;
        private int absorbedAmount;
        private int deletedAmount;

        private Builder() {}

        public Builder setChanneledImage(final ArrayList<CartesianPoint2D> channeledImage) {
            this.channeledImage = channeledImage;
            return this;
        }

        public Builder setPiercedImage(final ArrayList<CartesianPoint2D> piercedImage) {
            this.piercedImage = piercedImage;
            return this;
        }

        public Builder setChanneledCells(final ArrayList<ArrayList<Cell>> channeledCells) {
            this.channeledCells = channeledCells;
            return this;
        }

        public Builder setPiercedCells(final ArrayList<ArrayList<Cell>> piercedCells) {
            this.piercedCells = piercedCells;
            return this;
        }

        public Builder setChanneledCellsX(final ArrayList<Cell> channeledCellsX) {
            this.channeledCellsX = channeledCellsX;
            return this;
        }

        public Builder setPiercedCellsX(final ArrayList<Cell> piercedCellsX) {
            this.piercedCellsX = piercedCellsX;
            return this;
        }

        public Builder setChanneledCellsY(final ArrayList<Cell> channeledCellsY) {
            this.channeledCellsY = channeledCellsY;
            return this;
        }

        public Builder setPiercedCellsY(final ArrayList<Cell> piercedCellsY) {
            this.piercedCellsY = piercedCellsY;
            return this;
        }

        public Builder setChanneledAmount(int channeledAmount) {
            this.channeledAmount = channeledAmount;
            return this;
        }

        public Builder setPiercedAmount(int piercedAmount) {
            this.piercedAmount = piercedAmount;
            return this;
        }

        public Builder setOutOfDetectorAmount(int outOfDetectorAmount) {
            this.outOfDetectorAmount = outOfDetectorAmount;
            return this;
        }

        public Builder setAbsorbedAmount(int absorbedAmount) {
            this.absorbedAmount = absorbedAmount;
            return this;
        }

        public Builder setDeletedAmount(int deletedAmount) {
            this.deletedAmount = deletedAmount;
            return this;
        }

        public Distribution build() {
            return new Distribution(
                    channeledImage, piercedImage,
                    channeledCells, piercedCells,
                    channeledCellsX, piercedCellsX,
                    channeledCellsY, piercedCellsY,
                    channeledAmount, piercedAmount,
                    outOfDetectorAmount, absorbedAmount, deletedAmount);
        }
    }
}
