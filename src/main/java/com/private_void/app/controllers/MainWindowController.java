package com.private_void.app.controllers;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.app.notifiers.ProgressProvider;
import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.DivergentFlux;
import com.private_void.core.entities.fluxes.Flux;
import com.private_void.core.entities.fluxes.ParallelFlux;
import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.NeutralParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.plates.CurvedPlate;
import com.private_void.core.entities.plates.TorusFlatPlate;
import com.private_void.core.entities.surfaces.CapillarSystem;
import com.private_void.core.entities.surfaces.atomic_surfaces.AtomicPlane;
import com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.AtomicCylinder;
import com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.AtomicTorus;
import com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars.SingleAtomicCylinder;
import com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars.SingleAtomicTorus;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedCylinderFactory;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars.SmoothCylinder;
import com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars.SmoothTorus;
import com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCone;
import com.private_void.core.math.geometry.space_2D.CartesianPoint2D;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

import static com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCone.CONE_COEFFICIENT;
import static com.private_void.core.math.generators.Generator.generator;

public class MainWindowController extends CapStructController {

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
    public ScatterChart<Double, Double> chart;

    public NumberAxis yAxis;
    public NumberAxis xAxis;

    private ContextMenu menu;

    @FXML
    private void initialize() {
        pFluxAxisX.setText("1.0");
        pFluxAxisY.setText("-0.01");

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
        cylSlideAngle.setText("1");

        torRadius.setText("7");
        torBigRadius.setText("1000");
        torAngle.setText("1");
        torRoughSize.setText("0");
        torRoughAngle.setText("0");
        torReflect.setText("1");
        torSlideAngle.setText("1");

        coneRadius.setText("20");
        coneAngle.setText("30");
        coneCoefficient.setText("0.1");
        coneRoughSize.setText("0.2");
        coneRoughAngle.setText("5");
        coneReflect.setText("1");
        coneSlideAngle.setText("90");

        planePeriod.setText("0.1");
        planeChargeNum.setText("1");
        planeSize.setText("50");

        MenuItem clearChartItem = new MenuItem("Clear all");
        clearChartItem.setOnAction(actionEvent -> chart.getData().clear());
        menu = new ContextMenu(clearChartItem);

        final double SCALE_DELTA = 1.1;
        chart.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                menu.show(chart.getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });

        chart.setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

            chart.setScaleX(chart.getScaleX() * scaleFactor);
            chart.setScaleY(chart.getScaleY() * scaleFactor);
        });

        chart.setOnMousePressed(event -> {
            if (event.getClickCount() == 2) {
                chart.setScaleX(1.0);
                chart.setScaleY(1.0);
            }
        });
    }

    public void startBtnClick(ActionEvent actionEvent) {
        Service<Distribution> calculationService = new Service<Distribution>() {
            @Override
            protected Task<Distribution> createTask() {
                return new Task<Distribution>() {
                    @Override
                    protected Distribution call() {
                        ProgressProvider.getInstance()
                                .setProgressListener(progress -> updateProgress(progress, 100.0))
                                .setProgress(-1.0);

                        try {
                          return createPlate().interact(createFlux());
//                            return createCapillar().interact(createFlux());
                        } catch (Exception e) {
                            Logger.error(e.getCause().toString());
                            e.printStackTrace();
                            throw e;
                        }
                    }
                };
            }
        };

        ProgressDialogController progressDialogController = app.showProgressDialog();
        progressDialogController.bindProgressBar(calculationService);

        calculationService.setOnSucceeded(event -> {
            showImage(calculationService.getValue());
            progressDialogController.unbindProgressBar();
        });

        calculationService.setOnFailed(event -> {
            ProgressProvider.getInstance().setProgress(0);
            progressDialogController.unbindProgressBar();
        });

        calculationService.start();
    }

    private Flux createFlux() {
        Particle.Factory neutralParticleFactory = NeutralParticle.getFactory(1.0);
        Particle.Factory chargedParticleFactory = ChargedParticle.getFactory(1.0, 0.1, 4.0);

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

//            CartesianPoint.Factory circleUniformDistribution = generator().getXFlatCircleUniformDistribution(7.0);
//            CartesianPoint.Factory circleUniformDistribution = generator().getXFlatCircleUniformDistribution(150.0);
            CartesianPoint.Factory circleUniformDistribution = generator().getXFlatCircleUniformDistribution(370.0);

            return new ParallelFlux(
                    chargedParticleFactory,
                    circleUniformDistribution,
                    new CartesianPoint(x, y, z),
                    Vector.set(axisX, axisY, axisZ),
                    layersAmount,
                    particlesPerLayerAmount,
                    layerDistance,
                    minIntensity);
        } else {
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

    private CapillarSystem createPlate() {

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

            double plateCapillarsDensity = 0.0045;
//            double plateCapillarsDensity = 0.0043; //for curved radius = 7
//            double plateCapillarsDensity = 0.0034; //for flat radius = 7
//            double plateCapillarsDensity = 0.0003; //for radius = 20, count apr 20
//          double plateCapillarsDensity = 0.0025d; //for radius < 10 for domains

//          int capillarsAmount = 320;
            double plateSideLength = 300.0;

            StraightCapillarFactory smoothCylinderFactory = SmoothCylinder.getCapillarFactory(
                    capillarRadius,
                    capillarLength,
                    capillarRoughnessSize,
                    capillarRougnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            RotatedCylinderFactory rotatedSmoothCylinderFactory = SmoothCylinder.getRotatedCapillarFactory(
                    capillarRadius,
                    capillarLength,
                    capillarRoughnessSize,
                    capillarRougnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            int atomicChainsAmount = 1000;
            AtomicChain.Factory atomicChainFactory = AtomicChain.getFactory(2.0 * Math.PI / atomicChainsAmount);
            StraightCapillarFactory atomicCylinderFactory = AtomicCylinder.getCapillarFactory(
                    atomicChainFactory,
                    atomicChainsAmount,
                    1.0,
                    capillarRadius,
                    capillarLength);

            RotatedCylinderFactory rotatedAtomicCylinderFactory = AtomicCylinder.getRotatedCapillarFactory(
                    atomicChainFactory,
                    atomicChainsAmount,
                    1.0,
                    capillarRadius,
                    capillarLength
            );

//            return new FlatPlate(
//                    atomicCylinderFactory,
//                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
//                    plateCapillarsDensity,
//                    plateSideLength);

//            return new InclinedPlate(
//                    rotatedAtomicCylinderFactory,
//                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
//                    plateCapillarsDensity,
//                    plateSideLength);

            return new CurvedPlate(
                    rotatedAtomicCylinderFactory,
                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
                    plateCapillarsDensity,
                    Math.toRadians(1.0),
                    capillarLength * 20);
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

            // 0.0034 is critical 0.67 value for random or cell generation
            double plateCapillarsDensity = 0.0045; //for radius = 7
//          double plateCapillarsDensity = 0.0025; //for radius < 10 for domains

            double plateSideLength = 300.0;

//          int capillarsAmount = 320;
            double plateMaxAngleR = Math.toRadians(3.0);

            RotatedTorusFactory rotatedSmoothTorusFactory = SmoothTorus.getRotatedCapillarFactory(
                    capillarSmallRadius,
                    1000.0,
                    capillarRoughnessSize,
                    capillarRoughnessAngleR,
                    capillarReflectivity,
                    capillarCriticalAngleR);

            int atomicChainsAmount = 1000;
            AtomicChain.Factory atomicChainFactory = AtomicChain.getFactory(2.0 * Math.PI / atomicChainsAmount);
            RotatedTorusFactory rotatedAtomicTorusFactory = AtomicTorus.getRotatedCapillarFactory(
                    atomicChainFactory,
                    atomicChainsAmount,
                    1.0,
                    capillarSmallRadius,
                    1000.0
            );

            return new TorusFlatPlate(
                    rotatedAtomicTorusFactory,
                    new CartesianPoint(plateCenterX, plateCenterY, plateCenterZ),
                    plateCapillarsDensity,
                    capillarCurvAngleR);
        }

//        if (planeTab.isSelected()) {
//            double frontX = Double.parseDouble(planeX.getText());
//            double frontY = Double.parseDouble(planeY.getText());
//            double frontZ = Double.parseDouble(planeZ.getText());
//            double size = Double.parseDouble(planeSize.getText());
//
////            return new SmoothPlane(
////                    new CartesianPoint(frontX, frontY, frontZ),
////                    size,
////                    0.0,
////                    0.0,
////                    1,
////                    90);
//
//            return new SmoothTwoParallelPlanes(
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    size,
//                    10.0,
//                    0.0,
//                    0.0,
//                    1,
//                    90);
//        }

        if (planeTab.isSelected()) {
            double frontX = Double.parseDouble(planeX.getText());
            double frontY = Double.parseDouble(planeY.getText());
            double frontZ = Double.parseDouble(planeZ.getText());
            double size = Double.parseDouble(planeSize.getText());

            double period = Double.parseDouble(planePeriod.getText());
            double chargeNumber = Double.parseDouble(planeChargeNum.getText());

            return new AtomicPlane(
                    new CartesianPoint(frontX, frontY, frontZ),
                    period,
                    chargeNumber,
                    size);

//            return new AtomicTwoParallelPlanes(
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    period,
//                    10.0,
//                    chargeNumber,
//                    size);
        }

        return null;
    }

    private CapillarSystem createCapillar() {

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

//            return new SingleSmoothCylinder(
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    radius,
//                    length,
//                    roughnessSize,
//                    roughnessAngleR,
//                    reflectivity,
//                    criticalAngleR);

            int atomicChainsAmount = 1000;
            AtomicChain.Factory factory = AtomicChain.getFactory(2.0 * Math.PI / atomicChainsAmount);

            return new SingleAtomicCylinder(
                    new CartesianPoint(frontX, frontY, frontZ),
                    factory,
                    atomicChainsAmount,
                    1.0,
                    radius,
                    length);
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

//            return new SingleSmoothTorus(
//                    new CartesianPoint(frontX, frontY, frontZ),
//                    smallRadius,
//                    bigRadius,
//                    curvAngleR,
//                    roughnessSize,
//                    roughnessAngleR,
//                    reflectivity,
//                    criticalAngleR);

            int atomicChainsAmount = 1000;
            AtomicChain.Factory factory = AtomicChain.getFactory(2.0 * Math.PI / atomicChainsAmount);

            return new SingleAtomicTorus(
                    new CartesianPoint(frontX, frontY, frontZ),
                    factory,
                    atomicChainsAmount,
                    1.0,
                    smallRadius,
                    bigRadius,
                    curvAngleR);
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

        return null;
    }

    private void showImage(final Distribution distribution) {
        Logger.info(MessagePool.renderingStart());

        showChanneledImage(distribution);
        showPiercedImage(distribution);
        setChartScale(distribution.getWidth() / 2.0);

        Logger.info(MessagePool.renderingFinish());
        ProgressProvider.getInstance().setProgress(100.0);

        Logger.info(MessagePool.totalChanneleddAmount(distribution.getChanneledAmount()));
        Logger.info(MessagePool.totalPiercedAmount(distribution.getPiercedAmount()));
        Logger.info(MessagePool.totalOutOfDetector(distribution.getOutOfDetectorAmount()));
        Logger.info(MessagePool.totalAbsorbededAmount(distribution.getAbsorbedAmount()));
        Logger.info(MessagePool.totalDeletedAmount(distribution.getDeletedAmount()));
    }

    private void showChanneledImage(final Distribution distribution) {
        XYChart.Series<Double, Double> channeledImage = new XYChart.Series<>();
        for (CartesianPoint2D point : distribution.getChanneledImage()) {
            channeledImage.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }
        chart.getData().addAll(channeledImage);
    }

    private void showPiercedImage(final Distribution distribution) {
        XYChart.Series<Double, Double> piercedImage = new XYChart.Series<>();
        for (CartesianPoint2D point : distribution.getPiercedImage()) {
            piercedImage.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
        }
        chart.getData().addAll(piercedImage);
    }

    private void setChartScale(double width) {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        xAxis.setUpperBound(width);
        yAxis.setUpperBound(width);

        xAxis.setLowerBound(-width);
        yAxis.setLowerBound(-width);
    }
}

// Так можно для каждой точки отдельно задавать стиль
//for (int index = 0; index < series.getData().size(); index++) {
//        XYChart.Data dataPoint = series.getData().get(index);
//        Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
//        lineSymbol.setStyle("-fx-background-color: #00ff00, #000000; -fx-background-insets: 0, 2;\n" +
//        "    -fx-background-radius: 3px;\n" +
//        "    -fx-padding: 3px;");
//}