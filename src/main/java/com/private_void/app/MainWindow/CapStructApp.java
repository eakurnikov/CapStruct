package com.private_void.app.MainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CapStructApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/FXML/MainWindow.fxml";
        Parent root = new FXMLLoader().load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(0, "styles/styles.css");

        stage.setTitle("CapStruct");
        stage.setScene(scene);
        stage.show();
    }
}