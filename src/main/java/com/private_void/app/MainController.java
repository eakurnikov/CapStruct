package com.private_void.app;

import com.private_void.core.surfaces.simplesurfaces.capillars.Cone;
import com.private_void.core.surfaces.simplesurfaces.capillars.Cylinder;
import com.private_void.core.surfaces.simplesurfaces.SimpleSurface;
import com.private_void.core.surfaces.simplesurfaces.capillars.Torus;
import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.RotatedDetector;
import com.private_void.core.fluxes.DivergentFlux;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.fluxes.ParallelFlux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

import static com.private_void.utils.Constants.CONE_COEFFICIENT;

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
    public ScatterChart chart;

    @FXML
    public Label intensityOn;
    public Label intensityAbsorbed;
    public Label intensityOutOfCap;
    public Label intensityOutOfDet;
    public Label successLabel;

    public NumberAxis yAxis;
    public NumberAxis xAxis;

    @FXML
    private void initialize() {
        pFluxAxisX.setText("1.0");
        pFluxAxisY.setText("1.0");

        pFluxParticlesAmount.setText("1000");
        pFluxLayersAmount.setText("10");
        pFluxLayersDist.setText("1");
        pFluxMinIntensity.setText("0.5");

        dFluxAxisX.setText("1.0");
        dFluxParticlesAmount.setText("1000");
        dFluxAngle.setText("20");
        dFluxMinIntensity.setText("0.5");

        cylRadius.setText("20");
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
    }

    public void startBtnClick(ActionEvent actionEvent) {
        Flux flux = createFlux();
        SimpleSurface capillar = createCapillar();
        capillar.interact(flux);
        showResult(flux, capillar);
    }

    private Flux createFlux() {
        if (pFluxTab.isSelected()) {
            return new ParallelFlux(
                    NeutralParticle.getFactory(),
                    new Point3D(Float.parseFloat(pFluxX.getText()),
                                Float.parseFloat(pFluxY.getText()),
                                Float.parseFloat(pFluxZ.getText())),
                    new Vector3D(Float.parseFloat(pFluxAxisX.getText()),
                                 Float.parseFloat(pFluxAxisY.getText()),
                                 Float.parseFloat(pFluxAxisZ.getText())),
                    Integer.parseInt(pFluxLayersAmount.getText()),
                    Integer.parseInt(pFluxParticlesAmount.getText()),
                    Float.parseFloat(pFluxLayersDist.getText()),
                    Float.parseFloat(pFluxMinIntensity.getText())
            );
        }
        else {
            return new DivergentFlux(
                    NeutralParticle.getFactory(),
                    new Point3D(Float.parseFloat(dFluxX.getText()),
                                Float.parseFloat(dFluxY.getText()),
                                Float.parseFloat(dFluxZ.getText())),
                    new Vector3D(Float.parseFloat(dFluxAxisX.getText()),
                                 Float.parseFloat(dFluxAxisY.getText()),
                                 Float.parseFloat(dFluxAxisZ.getText())),
                    Integer.parseInt(dFluxParticlesAmount.getText()),
                    Float.parseFloat(dFluxAngle.getText()),
                    Float.parseFloat(dFluxMinIntensity.getText())
            );
        }
    }

    private SimpleSurface createCapillar() {
        if (cylTab.isSelected()) {
            return new Cylinder(
                    new Point3D(Float.parseFloat(cylX.getText()),
                                Float.parseFloat(cylY.getText()),
                                Float.parseFloat(cylZ.getText())),
                    Float.parseFloat(cylRadius.getText()),
                    Float.parseFloat(cylLength.getText()),
                    Float.parseFloat(cylRoughSize.getText()),
                    Float.parseFloat(cylRoughAngle.getText()),
                    Float.parseFloat(cylReflect.getText()),
                    Float.parseFloat(cylSlideAngle.getText())
            );
        } else if (torusTab.isSelected()) {
            return new Torus(
                    new Point3D(Float.parseFloat(torX.getText()),
                                Float.parseFloat(torY.getText()),
                                Float.parseFloat(torZ.getText())),
                    Float.parseFloat(torRadius.getText()),
                    Float.parseFloat(torBigRadius.getText()),
                    Float.parseFloat(torAngle.getText()),
                    Float.parseFloat(torRoughSize.getText()),
                    Float.parseFloat(torRoughAngle.getText()),
                    Float.parseFloat(torReflect.getText()),
                    Float.parseFloat(torSlideAngle.getText())
            );
        } else {
            try {
                return new Cone(
                        new Point3D(Float.parseFloat(coneX.getText()),
                                Float.parseFloat(coneY.getText()),
                                Float.parseFloat(coneZ.getText())),
                        Float.parseFloat(coneRadius.getText()),
                        Float.parseFloat(coneAngle.getText()),
                        Float.parseFloat(coneCoefficient.getText()),
                        Float.parseFloat(coneRoughSize.getText()),
                        Float.parseFloat(coneRoughAngle.getText()),
                        Float.parseFloat(coneReflect.getText()),
                        Float.parseFloat(coneSlideAngle.getText())
                );
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return new Cone(
                        new Point3D(Float.parseFloat(coneX.getText()),
                                Float.parseFloat(coneY.getText()),
                                Float.parseFloat(coneZ.getText())),
                        Float.parseFloat(coneRadius.getText()),
                        Float.parseFloat(coneAngle.getText()),
                        CONE_COEFFICIENT,
                        Float.parseFloat(coneRoughSize.getText()),
                        Float.parseFloat(coneRoughAngle.getText()),
                        Float.parseFloat(coneReflect.getText()),
                        Float.parseFloat(coneSlideAngle.getText())
                );
            }
        }
    }

    private void showResult(Flux flux, SimpleSurface capillar) {
        Detector detector = capillar.getDetector();
        XYChart.Series series = new XYChart.Series();
        series.setName("Particles");
        if (detector instanceof RotatedDetector) {
            float angle = -((RotatedDetector) detector).getAngle();
            for (Particle p : flux.getParticles()) {
                Point3D rotatedCoordinate = p.getProjection(angle);
                series.getData().add(new XYChart.Data(detector.getCenterCoordinate().getZ() + rotatedCoordinate.getZ(), rotatedCoordinate.getY()));
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

        intensityOn.setText(String.valueOf(detector.getDetectedIntensity()));
        intensityAbsorbed.setText(String.valueOf(detector.getAbsorbedIntensity()));
        intensityOutOfCap.setText(String.valueOf(detector.getOutOfCapillarIntensity()));
        intensityOutOfDet.setText(String.valueOf(detector.getOutOfDetectorIntensity()));

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