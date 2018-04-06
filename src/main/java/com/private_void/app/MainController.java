package com.private_void.app;

import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.RotatedDetector;
import com.private_void.core.fluxes.DivergentFlux;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.fluxes.ParallelFlux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.*;
import com.private_void.core.plates.CurvedPlate;
import com.private_void.core.plates.InclinedPlate;
import com.private_void.core.plates.Plate;
import com.private_void.core.plates.TorusPlate;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothCylinder;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothTorus;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars.RotatedCapillar;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCapillar;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCone;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCylinder;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothTorus;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars.RotatedSmoothCylinder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import static com.private_void.utils.Constants.CONE_COEFFICIENT;
import static com.private_void.utils.Generator.generator;

public class MainController {
    @FXML
    public Tab pFluxTab;
    public TextField pFluxX;
    public TextField pFluxY;
    public TextField pFluxZ;
    public TextField pFluxAxisX;
    public TextField pFluxAxisY;
    public TextField pFluxAxisZ;
    public TextField pFluxParticlesAmount;
    public TextField pFluxLayersAmount;
    public TextField pFluxLayersDist;
    public TextField pFluxMinIntensity;

    @FXML
    public Tab dFluxTab;
    public TextField dFluxX;
    public TextField dFluxY;
    public TextField dFluxZ;
    public TextField dFluxAxisX;
    public TextField dFluxAxisY;
    public TextField dFluxAxisZ;
    public TextField dFluxParticlesAmount;
    public TextField dFluxAngle;
    public TextField dFluxMinIntensity;

    @FXML
    public Tab cylTab;
    public TextField cylX;
    public TextField cylY;
    public TextField cylZ;
    public TextField cylRadius;
    public TextField cylLength;
    public TextField cylRoughSize;
    public TextField cylRoughAngle;
    public TextField cylReflect;
    public TextField cylSlideAngle;

    @FXML
    public Tab torusTab;
    public TextField torX;
    public TextField torY;
    public TextField torZ;
    public TextField torRadius;
    public TextField torBigRadius;
    public TextField torAngle;
    public TextField torRoughSize;
    public TextField torRoughAngle;
    public TextField torReflect;
    public TextField torSlideAngle;

    @FXML
    public Tab coneTab;
    public TextField coneX;
    public TextField coneY;
    public TextField coneZ;
    public TextField coneRadius;
    public TextField coneAngle;
    public TextField coneCoefficient;
    public TextField coneRoughSize;
    public TextField coneRoughAngle;
    public TextField coneReflect;
    public TextField coneSlideAngle;

    @FXML
    public Tab planeTab;
    public TextField planeX;
    public TextField planeY;
    public TextField planeZ;
    public TextField planePeriod;
    public TextField planeChargeNum;
    public TextField planeSize;

    @FXML
    public ScatterChart chart;

    @FXML
    public Label detectedAmount;
    public Label absorbedAmount;
    public Label outOfCapillarsAmount;
    public Label outOfDetectorAmount;
    public Label successLabel;

    public NumberAxis yAxis;
    public NumberAxis xAxis;

    public ContextMenu menu;
    public MenuItem clearChartItem;

    @FXML
    private void initialize() {
        pFluxAxisX.setText("1.0");
        pFluxAxisY.setText("-0.0");

        pFluxParticlesAmount.setText("1000");
        pFluxLayersAmount.setText("10");
        pFluxLayersDist.setText("1");
        pFluxMinIntensity.setText("0.5");

        dFluxAxisX.setText("1.0");
        dFluxParticlesAmount.setText("1000");
        dFluxAngle.setText("20");
        dFluxMinIntensity.setText("0.5");

        cylRadius.setText("7");
        cylLength.setText("1000");
        cylRoughSize.setText("0");
        cylRoughAngle.setText("0");
        cylReflect.setText("1");
        cylSlideAngle.setText("90");

        torRadius.setText("20");
        torBigRadius.setText("1000");
        torAngle.setText("5");
        torRoughSize.setText("0.2");
        torRoughAngle.setText("5");
        torReflect.setText("1");
        torSlideAngle.setText("90");

        coneRadius.setText("20");
        coneAngle.setText("30");
        coneCoefficient.setText("0.1");
        coneRoughSize.setText("0.2");
        coneRoughAngle.setText("5");
        coneReflect.setText("1");
        coneSlideAngle.setText("90");

        planePeriod.setText("1");
        planeChargeNum.setText("1");
        planeSize.setText("50");

        clearChartItem = new MenuItem("Clear all");
        clearChartItem.setOnAction((actionEvent) -> chart.getData().clear());
        menu = new ContextMenu(clearChartItem);
        chart.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                menu.show(chart.getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });
    }

    public void startBtnClick(ActionEvent actionEvent) {
        successLabel.setVisible(false);

        Flux flux = createFlux();
        CapillarSystem system = createPlate();
//        CapillarSystem system = createCapillar();
        Detector detector = system.getDetector();

        system.interact(flux);
//        if (system instanceof Plate) {
//            ((Plate) system).testInteract(flux);
//        }
//        system.interactStream(flux);
//        system.interactParallel(flux);
//        system.interactFork(flux);

        detector.detect(flux);
        showResult(flux, detector);
    }

    private Flux createFlux() {
        Particle.Factory neutralParticleFactory = NeutralParticle.getFactory(1.0);

        if (pFluxTab.isSelected()) {
            double x = Double.parseDouble(pFluxX.getText());
            double y = Double.parseDouble(pFluxY.getText());
            double z = Double.parseDouble(pFluxZ.getText());

            double axisX = Double.parseDouble(pFluxAxisX.getText());
            double axisY = Double.parseDouble(pFluxAxisY.getText());
            double axisZ = Double.parseDouble(pFluxAxisZ.getText());

            int layersAmount = Integer.parseInt(pFluxLayersAmount.getText());
            int particlesPerLayerAmount = Integer.parseInt(pFluxParticlesAmount.getText());
            double layerDistance = Double.parseDouble(pFluxLayersDist.getText());
            double minIntensity = Double.parseDouble(pFluxMinIntensity.getText());

            CartesianPoint.Factory gaussDistribution = generator().getGaussDistribution(0.0, 1.0);

            CartesianPoint.Factory uniformDistribution = generator().getXFlatUniformDistribution(250.0, 250.0);

            CartesianPoint.Factory circleUniformDistribution = generator().getXFlatCircleUniformDistribution(150.0);

            return new ParallelFlux(
                    neutralParticleFactory,
                    circleUniformDistribution,
                    new CartesianPoint(x, y, z),
                    Vector.set(axisX, axisY, axisZ),
                    layersAmount,
                    particlesPerLayerAmount,
                    layerDistance,
                    minIntensity);
        }
        else {
            CartesianPoint.Factory gaussDistribution = generator().getGaussDistribution(0.0,
                    Math.toRadians(Double.parseDouble(dFluxAngle.getText())));

            double x = Double.parseDouble(dFluxX.getText());
            double y = Double.parseDouble(dFluxY.getText());
            double z = Double.parseDouble(dFluxZ.getText());

            double axisX = Double.parseDouble(dFluxAxisX.getText());
            double axisY = Double.parseDouble(dFluxAxisY.getText());
            double axisZ = Double.parseDouble(dFluxAxisZ.getText());

            int totalParticlesAmount = Integer.parseInt(dFluxParticlesAmount.getText());
            double minIntensity = Double.parseDouble(dFluxMinIntensity.getText());

            return new DivergentFlux(
                    neutralParticleFactory,
                    gaussDistribution,
                    new CartesianPoint(x, y, z),
                    Vector.set(axisX, axisY, axisZ),
                    totalParticlesAmount,
                    minIntensity);
        }
    }

    private Plate createPlate() {

        if (cylTab.isSelected()) {
            double plateCenterX = Double.parseDouble(cylX.getText());
            double plateCenterY = Double.parseDouble(cylY.getText());
            double plateCenterZ = Double.parseDouble(cylZ.getText());

            double capillarRadius = Double.parseDouble(cylRadius.getText());
            double capillarLength = Double.parseDouble(cylLength.getText());
            double capillarRoughnessSize = Double.parseDouble(cylRoughSize.getText());
            double capillarRougnessAngleD = Double.parseDouble(cylRoughAngle.getText());
            double capillarRougnessAngleR = Math.toRadians(capillarRougnessAngleD);
            double capillarReflectivity = Double.parseDouble(cylReflect.getText());
            double capillarCriticalAngleD = Double.parseDouble(cylSlideAngle.getText());
            double capillarCriticalAngleR = Math.toRadians(capillarCriticalAngleD);

            double plateCapillarsDensity = 0.0034; //for radius = 7
//            double plateCapillarsDensity = 0.0003; //for radius = 20, count apr 20
//          double plateCapillarsDensity = 0.0025d; //for radius < 10 for domains

//          int capillarsAmount = 320;
            double plateSideLength = 300.0;

            Capillar.Factory smoothCylinderFactory = SmoothCylinder.getFactory(
                    capillarRadius,
                    capillarLength,
                    capillarRoughnessSize,
                    capillarRougnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            RotatedCapillar.Factory rotatedSmoothCylinderFactory = RotatedSmoothCylinder.getFactory(
                    capillarRadius,
                    capillarLength,
                    capillarRoughnessSize,
                    capillarRougnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

//            return new FlatPlate(
//                    smoothCylinderFactory,
//                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
//                    plateCapillarsDensity,
//                    plateSideLength);

//            return new InclinedPlate(
//                    rotatedSmoothCylinderFactory,
//                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
//                    plateCapillarsDensity,
//                    plateSideLength);

            return new CurvedPlate(
                    rotatedSmoothCylinderFactory,
                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
                    plateCapillarsDensity,
                    Math.toRadians(1.0),
                    50_000.0);
        }

        if (torusTab.isSelected()) {
            double plateCenterX = Double.parseDouble(torX.getText());
            double plateCenterY = Double.parseDouble(torY.getText());
            double plateCenterZ = Double.parseDouble(torZ.getText());

            double capillarSmallRadius = Double.parseDouble(torRadius.getText());
            double capillarBigRadius = Double.parseDouble(torBigRadius.getText());
            double capillarCurvAngleD = Double.parseDouble(torAngle.getText());
            double capillarCurvAngleR = Math.toRadians(capillarCurvAngleD);
            double capillarRoughnessSize = Double.parseDouble(torRoughSize.getText());
            double capillarRoughnessAngleD = Double.parseDouble(torRoughAngle.getText());
            double capillarRoughnessAngleR = Math.toRadians(capillarRoughnessAngleD);
            double capillarReflectivity = Double.parseDouble(torReflect.getText());
            double capillarCriticalAngleD = Double.parseDouble(torSlideAngle.getText());
            double capillarCriticalAngleR = Math.toRadians(capillarCriticalAngleD);

            double plateCapillarsDensity = 0.0034; //for radius = 7
//          double plateCapillarsDensity = 0.0025; //for radius < 10 for domains

            double plateSideLength = 300.0;

//          int capillarsAmount = 320;
            double plateMaxAngleR = 5.0;

            Capillar.Factory smoothTorusFactoryWithRadius = SmoothTorus.getFactory(
                    capillarSmallRadius,
                    1000.0,
                    capillarCurvAngleR,
                    capillarRoughnessSize,
                    capillarRoughnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            Capillar.Factory smoothTorusFactoryWithLength = SmoothTorus.getFactoryWithLength(
                    capillarSmallRadius,
                    1000.0,
                    capillarCurvAngleR,
                    capillarRoughnessSize,
                    capillarRoughnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            return new TorusPlate(
                    smoothTorusFactoryWithRadius,
                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
                    plateCapillarsDensity,
                    plateSideLength,
                    plateMaxAngleR);
        }

        return null;
    }

    private SingleSmoothCapillar createCapillar() {

        if (cylTab.isSelected()) {
            double frontX = Double.parseDouble(cylX.getText());
            double frontY = Double.parseDouble(cylY.getText());
            double frontZ = Double.parseDouble(cylZ.getText());

            double radius = Double.parseDouble(cylRadius.getText());
            double length = Double.parseDouble(cylLength.getText());
            double roughnessSize = Double.parseDouble(cylRoughSize.getText());
            double roughnessAngleD = Double.parseDouble(cylRoughAngle.getText());
            double roughnessAngleR = Math.toRadians(roughnessAngleD);
            double reflectivity = Double.parseDouble(cylReflect.getText());
            double criticalAngleD = Double.parseDouble(cylSlideAngle.getText());
            double criticalAngleR = Math.toRadians(criticalAngleD);

            return new SingleSmoothCylinder(
                    new CartesianPoint(frontX, frontY, frontZ),
                    radius,
                    length,
                    roughnessSize,
                    roughnessAngleR,
                    reflectivity,
                    criticalAngleR);
        }

        if (torusTab.isSelected()) {
            double frontX = Double.parseDouble(torX.getText());
            double frontY = Double.parseDouble(torY.getText());
            double frontZ = Double.parseDouble(torZ.getText());

            double smallRadius = Double.parseDouble(torRadius.getText());
            double bigRadius = Double.parseDouble(torBigRadius.getText());
            double curvAngleD = Double.parseDouble(torAngle.getText());
            double curvAngleR = Math.toRadians(curvAngleD);
            double roughnessSize = Double.parseDouble(torRoughSize.getText());
            double roughnessAngleD = Double.parseDouble(torRoughAngle.getText());
            double roughnessAngleR = Math.toRadians(roughnessAngleD);
            double reflectivity = Double.parseDouble(torReflect.getText());
            double criticalAngleD = Double.parseDouble(torSlideAngle.getText());
            double criticalAngleR = Math.toRadians(criticalAngleD);

            return new SingleSmoothTorus(
                    new CartesianPoint(frontX, frontY, frontZ),
                    smallRadius,
                    bigRadius,
                    curvAngleR,
                    roughnessSize,
                    roughnessAngleR,
                    reflectivity,
                    criticalAngleR);
        }

        if (coneTab.isSelected()) {
            double frontX = Double.parseDouble(coneX.getText());
            double frontY = Double.parseDouble(coneY.getText());
            double frontZ = Double.parseDouble(coneZ.getText());

            double radius = Double.parseDouble(this.coneRadius.getText());
            double length = Double.parseDouble(coneAngle.getText()); //now the second constructor is used (with length)
            double coneCoefficient = Double.parseDouble(this.coneCoefficient.getText());
            double roughnessSize = Double.parseDouble(coneRoughSize.getText());
            double roughnessAngleD = Double.parseDouble(coneRoughAngle.getText());
            double roughnessAngleR = Math.toRadians(roughnessAngleD);
            double reflectivity = Double.parseDouble(coneReflect.getText());
            double criticalAngleD = Double.parseDouble(coneSlideAngle.getText());
            double criticalAngleR = Math.toRadians(criticalAngleD);

            try {
                return new SingleSmoothCone(
                        new CartesianPoint(frontX, frontY, frontZ),
                        radius,
                        length,
                        coneCoefficient,
                        roughnessSize,
                        roughnessAngleR,
                        reflectivity,
                        criticalAngleR);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();

                return new SingleSmoothCone(
                        new CartesianPoint(frontX, frontY, frontZ),
                        radius,
                        length,
                        CONE_COEFFICIENT,
                        roughnessSize,
                        roughnessAngleR,
                        reflectivity,
                        criticalAngleR);
            }
        }

//        if (planeTab.isSelected()) {
//            double frontX = Double.parseDouble(planeX.getText());
//            double frontY = Double.parseDouble(planeY.getText());
//            double frontZ = Double.parseDouble(planeZ.getText());
//            double size = Double.parseDouble(planeSize.getText());
//
//            return new SmoothPlane(
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    size,
//                    0.2,
//                    5,
//                    1,
//                    90);
//        }

//        if (planeTab.isSelected()) {
//            AtomFactory atomFactory = Atom.getFactory();
//            double frontX = Double.parseDouble(planeX.getText());
//            double frontY = Double.parseDouble(planeY.getText());
//            double frontZ = Double.parseDouble(planeZ.getText());
//            double size = Double.parseDouble(planeSize.getText());
//
//            double period = Double.parseDouble(planePeriod.getText());
//            double chargeNumber = Double.parseDouble(planeChargeNum.getText());
//            return new AtomicPlane(
//                    atomFactory,
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    period,
//                    chargeNumber,
//                    size);
//        }

        return null;
    }

    private void showResult(Flux flux, Detector detector) {
        XYChart.Series series = new XYChart.Series();
        series.setName("Particles");
        if (detector instanceof RotatedDetector) {
            double angle = -((RotatedDetector) detector).getAngle();
            for (Particle p : flux.getParticles()) {
                CartesianPoint rotatedCoordinate = p.rotateCoordinateAroundOY(angle);
                series.getData().add(new XYChart.Data(detector.getCenter().getZ() + rotatedCoordinate.getZ(), rotatedCoordinate.getY()));
            }
        } else {
            for (Particle p : flux.getParticles()) {
                series.getData().add(new XYChart.Data(p.getCoordinate().getZ(), p.getCoordinate().getY()));
            }
        }

        //TODO прикрутить зум по сколлу колесика мыши
        //TODO как-то разукрашивать точки в зависимости от их интенсивности. Тогда детектор как счетчик интенсивности со своими ячейками ваще не нужен, нужна будет тупо его плоскость
        chart.getData().addAll(series);
        setChartScale(detector.getUpperBound(), detector.getLowerBound());

        detectedAmount.setText(String.valueOf(detector.getDetectedAmount()));
        absorbedAmount.setText(String.valueOf(detector.getAbsorbedAmount()));
        outOfCapillarsAmount.setText(String.valueOf(detector.getOutOfCapillarsAmount()));
        outOfDetectorAmount.setText(String.valueOf(detector.getOutOfDetectorAmount()));

        successLabel.setVisible(true);
    }

    private void setChartScale(double upperBound, double lowerBound) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        xAxis.setUpperBound(1.2 * upperBound);
        yAxis.setUpperBound(1.2 * upperBound);

        xAxis.setLowerBound(1.2 * lowerBound);
        yAxis.setLowerBound(1.2 * lowerBound);
    }
}