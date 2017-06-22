package com.private_void.app;

import com.private_void.core.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        String fxmlFile = "/FXML/MainScene.fxml";
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
//
//        stage.setTitle("CapStruct");
//        stage.setScene(new Scene(root));
//        stage.show();

        ParallelFlux pFlux = new ParallelFlux(new Point3D(0.0f, 0.0f, 0.0f),
                                              new Vector3D(1.0f, -1.0f, 0.001f),
                                             20, 100, 0.2f, 0.5f);

        DivergentFlux dFlux = new DivergentFlux(new Point3D(0.0f, 0.0f, 0.0f),
                                               new Vector3D(1.0f, 0.0f, 0.0f),
                                              1000, 20, 0.5f);

        Cylinder cylinder = new Cylinder(new Point3D(0.0f, 0.0f, 0.0f),
                                        20.0f, 100.0f, 0.0f, 0.0f, 1.0f, 5.0f);
        try {
            pFlux = (ParallelFlux) cylinder.passThrough(pFlux);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final NumberAxis xAxis = new NumberAxis(-25, 25, 1);
        final NumberAxis yAxis = new NumberAxis(-25, 25, 1);
        final ScatterChart<Number,Number> sc = new ScatterChart<>(xAxis,yAxis);

        xAxis.setLabel("X");
        yAxis.setLabel("Y");
        sc.setTitle("Distribution");

        XYChart.Series series = new XYChart.Series();
        series.setName("Particles");
        for (Particle particle : dFlux.getParticles()) {
            series.getData().add(new XYChart.Data(particle.getCoordinate().getZ(), particle.getCoordinate().getY()));
        }

        sc.getData().addAll(series);
        Scene scene  = new Scene(sc, 700, 700);

        stage.setTitle("CapStruct");
        stage.setScene(scene);
        stage.show();
    }
}