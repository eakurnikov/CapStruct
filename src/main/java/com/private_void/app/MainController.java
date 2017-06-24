package com.private_void.app;

import com.private_void.core.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

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

    private Flux flux;
    private Surface capillar;

    @FXML
    private void initialize() {}

    public void startBtnClick(ActionEvent actionEvent) {
        flux = createFlux();
        capillar = createCapillar();
        flux = capillar.passThrough(flux);
        showResult(flux);
    }

    private Flux createFlux() {
        if (pFluxTab.isSelected()) {
            return new ParallelFlux(
                    new Point3D(Float.parseFloat(pFluxX.getText()),
                                Float.parseFloat(pFluxY.getText()),
                                Float.parseFloat(pFluxZ.getText())),

                    new Vector3D(Float.parseFloat(pFluxAxisX.getText()),
                                 Float.parseFloat(pFluxAxisX.getText()),
                                 Float.parseFloat(pFluxAxisX.getText())),

                    Integer.parseInt(pFluxLayersAmount.getText()),
                    Integer.parseInt(pFluxParticlesAmount.getText()),
                    Float.parseFloat(pFluxLayersDist.getText()),
                    Float.parseFloat(pFluxMinIntensity.getText())
            );
        }
        else {
            return new DivergentFlux(
                    new Point3D(Float.parseFloat(dFluxX.getText()),
                                Float.parseFloat(dFluxY.getText()),
                                Float.parseFloat(dFluxZ.getText())),

                    new Vector3D(Float.parseFloat(dFluxAxisX.getText()),
                                 Float.parseFloat(dFluxAxisX.getText()),
                                 Float.parseFloat(dFluxAxisX.getText())),

                    Integer.parseInt(dFluxParticlesAmount.getText()),
                    Float.parseFloat(dFluxAngle.getText()),
                    Float.parseFloat(dFluxMinIntensity.getText())
            );
        }
    }

    private Surface createCapillar() {
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
        }
    }

    private void showResult(Flux flux) {
        XYChart.Series series = new XYChart.Series();
        series.setName("Particles");
        for (Particle particle : flux.getParticles()) {
            series.getData().add(new XYChart.Data(particle.getCoordinate().getZ(), particle.getCoordinate().getY()));
        }
        chart.getData().addAll(series);
    }
}