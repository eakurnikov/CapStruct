package com.private_void.app;

import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.RotatedDetector;
import com.private_void.core.fluxes.DivergentFlux;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.fluxes.ParallelFlux;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.*;
import com.private_void.core.plates.FlatPlate;
import com.private_void.core.plates.Plate;
import com.private_void.core.plates.TorusPlate;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothCylinder;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothTorus;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCapillar;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCone;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCylinder;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothTorus;
import com.private_void.utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
        cylRoughSize.setText("0.1");
        cylRoughAngle.setText("5");
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
        clearChartItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chart.getData().clear();
            }
        });

        menu = new ContextMenu(clearChartItem);

        chart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    menu.show(chart.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                }
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
//        system.interactStream(flux);
//        system.interactParallel(flux);
//        system.interactFork(flux);

        detector.detect(flux);
        showResult(flux, detector);
    }

    private Flux createFlux() {
        ParticleFactory neutralParticleFactory = NeutralParticle.getFactory(1.0f);

        if (pFluxTab.isSelected()) {
            float x = Float.parseFloat(pFluxX.getText());
            float y = Float.parseFloat(pFluxY.getText());
            float z = Float.parseFloat(pFluxZ.getText());

            float axisX = Float.parseFloat(pFluxAxisX.getText());
            float axisY = Float.parseFloat(pFluxAxisY.getText());
            float axisZ = Float.parseFloat(pFluxAxisZ.getText());

            int layersAmount = Integer.parseInt(pFluxLayersAmount.getText());
            int particlesPerLayerAmount = Integer.parseInt(pFluxParticlesAmount.getText());
            float layerDistance = Float.parseFloat(pFluxLayersDist.getText());
            float minIntensity = Float.parseFloat(pFluxMinIntensity.getText());

            CoordinateFactory gaussDistribution = generator().getGaussDistribution(0.0f, 1.0f);

            CoordinateFactory uniformDistribution = generator().getXPlanarUniformDistribution(250.0f, 250.0f);

            CoordinateFactory circleUniformDistribution = generator().getXPlanarCircleUniformDistribution(150.0f);

            return new ParallelFlux(
                    neutralParticleFactory,
                    circleUniformDistribution,
                    new Point3D(x, y, z),
                    new Vector3D(axisX, axisY, axisZ),
                    layersAmount,
                    particlesPerLayerAmount,
                    layerDistance,
                    minIntensity);
        }
        else {
            CoordinateFactory gaussDistribution = generator().getGaussDistribution(0.0f,
                    Utils.convertDegreesToRadians(Float.parseFloat(dFluxAngle.getText())));

            float x = Float.parseFloat(dFluxX.getText());
            float y = Float.parseFloat(dFluxY.getText());
            float z = Float.parseFloat(dFluxZ.getText());

            float axisX = Float.parseFloat(dFluxAxisX.getText());
            float axisY = Float.parseFloat(dFluxAxisY.getText());
            float axisZ = Float.parseFloat(dFluxAxisZ.getText());

            int totalParticlesAmount = Integer.parseInt(dFluxParticlesAmount.getText());
            float minIntensity = Float.parseFloat(dFluxMinIntensity.getText());

            return new DivergentFlux(
                    neutralParticleFactory,
                    gaussDistribution,
                    new Point3D(x, y, z),
                    new Vector3D(axisX, axisY, axisZ),
                    totalParticlesAmount,
                    minIntensity);
        }
    }

    private Plate createPlate() {

        if (cylTab.isSelected()) {
            float plateCenterX = Float.parseFloat(cylX.getText());
            float plateCenterY = Float.parseFloat(cylY.getText());
            float plateCenterZ = Float.parseFloat(cylZ.getText());

            float capillarRadius = Float.parseFloat(cylRadius.getText());
            float capillarLength = Float.parseFloat(cylLength.getText());
            float capillarRoughnessSize = Float.parseFloat(cylRoughSize.getText());
            float capillarRougnessAngleD = Float.parseFloat(cylRoughAngle.getText());
            float capillarRougnessAngleR = Utils.convertDegreesToRadians(capillarRougnessAngleD);
            float capillarReflectivity = Float.parseFloat(cylReflect.getText());
            float capillarCriticalAngleD = Float.parseFloat(cylSlideAngle.getText());
            float capillarCriticalAngleR = Utils.convertDegreesToRadians(capillarCriticalAngleD);

            int capillarsAmount = 320;
            float plateCapillarsDensity = 0.0034f; //for radius = 7
//          float plateCapillarsDensity = 0.0025f; //for radius < 10 for domains

            CapillarFactory smoothCylinderFactory = SmoothCylinder.getFactory(
                    capillarRadius,
                    capillarLength,
                    capillarRoughnessSize,
                    capillarRougnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            return new FlatPlate(
                    smoothCylinderFactory,
                    new Point3D(plateCenterX, plateCenterY, plateCenterZ),
                    capillarsAmount,
                    plateCapillarsDensity);
        }

        if (torusTab.isSelected()) {
            float plateCenterX = Float.parseFloat(torX.getText());
            float plateCenterY = Float.parseFloat(torY.getText());
            float plateCenterZ = Float.parseFloat(torZ.getText());

            float capillarSmallRadius = Float.parseFloat(torRadius.getText());
            float capillarBigRadius = Float.parseFloat(torBigRadius.getText());
            float capillarCurvAngleD = Float.parseFloat(torAngle.getText());
            float capillarCurvAngleR = Utils.convertDegreesToRadians(capillarCurvAngleD);
            float capillarRoughnessSize = Float.parseFloat(torRoughSize.getText());
            float capillarRoughnessAngleD = Float.parseFloat(torRoughAngle.getText());
            float capillarRoughnessAngleR = Utils.convertDegreesToRadians(capillarRoughnessAngleD);
            float capillarReflectivity = Float.parseFloat(torReflect.getText());
            float capillarCriticalAngleD = Float.parseFloat(torSlideAngle.getText());
            float capillarCriticalAngleR = Utils.convertDegreesToRadians(capillarCriticalAngleD);

            int capillarsAmount = 320;
            float plateCapillarsDensity = 0.0034f; //for radius = 7
//          float plateCapillarsDensity = 0.0025f; //for radius < 10 for domains

            CapillarFactory smoothTorusFactoryWithRadius = SmoothTorus.getFactory(
                    capillarSmallRadius,
                    1000.0f,
                    capillarCurvAngleR,
                    capillarRoughnessSize,
                    capillarRoughnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            CapillarFactory smoothTorusFactoryWithLength = SmoothTorus.getFactoryWithLength(
                    capillarSmallRadius,
                    1000.0f,
                    capillarCurvAngleR,
                    capillarRoughnessSize,
                    capillarRoughnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            return new TorusPlate(
                    smoothTorusFactoryWithRadius,
                    new Point3D(plateCenterX, plateCenterY, plateCenterZ),
                    capillarsAmount,
                    plateCapillarsDensity, 5);
        }

        return null;
    }

    private SingleSmoothCapillar createCapillar() {

        if (cylTab.isSelected()) {
            float frontX = Float.parseFloat(cylX.getText());
            float frontY = Float.parseFloat(cylY.getText());
            float frontZ = Float.parseFloat(cylZ.getText());

            float radius = Float.parseFloat(cylRadius.getText());
            float length = Float.parseFloat(cylLength.getText());
            float roughnessSize = Float.parseFloat(cylRoughSize.getText());
            float roughnessAngleD = Float.parseFloat(cylRoughAngle.getText());
            float roughnessAngleR = Utils.convertDegreesToRadians(roughnessAngleD);
            float reflectivity = Float.parseFloat(cylReflect.getText());
            float criticalAngleD = Float.parseFloat(cylSlideAngle.getText());
            float criticalAngleR = Utils.convertDegreesToRadians(criticalAngleD);

            return new SingleSmoothCylinder(
                    new Point3D(frontX, frontY, frontZ),
                    radius,
                    length,
                    roughnessSize,
                    roughnessAngleR,
                    reflectivity,
                    criticalAngleR);
        }

        if (torusTab.isSelected()) {
            float frontX = Float.parseFloat(torX.getText());
            float frontY = Float.parseFloat(torY.getText());
            float frontZ = Float.parseFloat(torZ.getText());

            float smallRadius = Float.parseFloat(torRadius.getText());
            float bigRadius = Float.parseFloat(torBigRadius.getText());
            float curvAngleD = Float.parseFloat(torAngle.getText());
            float curvAngleR = Utils.convertDegreesToRadians(curvAngleD);
            float roughnessSize = Float.parseFloat(torRoughSize.getText());
            float roughnessAngleD = Float.parseFloat(torRoughAngle.getText());
            float roughnessAngleR = Utils.convertDegreesToRadians(roughnessAngleD);
            float reflectivity = Float.parseFloat(torReflect.getText());
            float criticalAngleD = Float.parseFloat(torSlideAngle.getText());
            float criticalAngleR = Utils.convertDegreesToRadians(criticalAngleD);

            return new SingleSmoothTorus(
                    new Point3D(frontX, frontY, frontZ),
                    smallRadius,
                    bigRadius,
                    curvAngleR,
                    roughnessSize,
                    roughnessAngleR,
                    reflectivity,
                    criticalAngleR);
        }

        if (coneTab.isSelected()) {
            float frontX = Float.parseFloat(coneX.getText());
            float frontY = Float.parseFloat(coneY.getText());
            float frontZ = Float.parseFloat(coneZ.getText());

            float radius = Float.parseFloat(this.coneRadius.getText());
            float length = Float.parseFloat(coneAngle.getText()); //now the second constructor is used (with length)
            float coneCoefficient = Float.parseFloat(this.coneCoefficient.getText());
            float roughnessSize = Float.parseFloat(coneRoughSize.getText());
            float roughnessAngleD = Float.parseFloat(coneRoughAngle.getText());
            float roughnessAngleR = Utils.convertDegreesToRadians(roughnessAngleD);
            float reflectivity = Float.parseFloat(coneReflect.getText());
            float criticalAngleD = Float.parseFloat(coneSlideAngle.getText());
            float criticalAngleR = Utils.convertDegreesToRadians(criticalAngleD);

            try {
                return new SingleSmoothCone(
                        new Point3D(frontX, frontY, frontZ),
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
                        new Point3D(frontX, frontY, frontZ),
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
//            float frontX = Float.parseFloat(planeX.getText());
//            float frontY = Float.parseFloat(planeY.getText());
//            float frontZ = Float.parseFloat(planeZ.getText());
//            float size = Float.parseFloat(planeSize.getText());
//
//            return new SmoothPlane(
//                    new Point3D(frontX, frontY, frontZ),
//                    size,
//                    0.2f,
//                    5f,
//                    1f,
//                    90f);
//        }

//        if (planeTab.isSelected()) {
//            AtomFactory atomFactory = Atom.getFactory();
//            float frontX = Float.parseFloat(planeX.getText());
//            float frontY = Float.parseFloat(planeY.getText());
//            float frontZ = Float.parseFloat(planeZ.getText());
//            float size = Float.parseFloat(planeSize.getText());
//
//            float period = Float.parseFloat(planePeriod.getText());
//            float chargeNumber = Float.parseFloat(planeChargeNum.getText());
//            return new AtomicPlane(
//                    atomFactory,
//                    new Point3D(frontX, frontY, frontZ),
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
            float angle = -((RotatedDetector) detector).getAngle();
            for (Particle p : flux.getParticles()) {
                Point3D rotatedCoordinate = p.getProjection(angle);
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

        xAxis.setUpperBound(1.2f * upperBound);
        yAxis.setUpperBound(1.2f * upperBound);

        xAxis.setLowerBound(1.2f * lowerBound);
        yAxis.setLowerBound(1.2f * lowerBound);
    }
}