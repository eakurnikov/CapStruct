package com.private_void.app;

import com.private_void.app.main_window.MainWindowController;
import com.private_void.app.progress_dialog.ProgressDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CapStructApp extends Application {
    private static final String MAIN_WINDOW_FXML_PATH = "/FXML/MainWindow.fxml";
    private static final String PROGRESS_DIALOG_FXML_PATH = "/FXML/ProgressDialog.fxml";

    private Stage mainWindowStage;
    private Stage progressDialogStage;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        this.mainWindowStage = mainStage;
        showMainWindow();
    }

    private void showMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent mainWindow = loader.load(getClass().getResourceAsStream(MAIN_WINDOW_FXML_PATH));
        ((CapStructController) loader.getController()).setApp(CapStructApp.this);

        Scene scene = new Scene(mainWindow);
        scene.getStylesheets().add(0, "styles/main_window_styles.css");

        mainWindowStage.setTitle("CapStruct");
        mainWindowStage.setScene(scene);
        mainWindowStage.show();
    }

    public ProgressDialogController showProgressDialog() {
        try {
            progressDialogStage = new Stage();
            progressDialogStage.initModality(Modality.WINDOW_MODAL);
            progressDialogStage.initOwner(mainWindowStage);
            progressDialogStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader();
            Parent dialog = loader.load(getClass().getResourceAsStream(PROGRESS_DIALOG_FXML_PATH));
            ProgressDialogController controller = loader.getController();
            controller.setApp(CapStructApp.this);

            Scene scene = new Scene(dialog);
            scene.getStylesheets().add(0, "styles/progress_dialog_styles.css");

            progressDialogStage.setTitle("Computing");
            progressDialogStage.setScene(scene);
            progressDialogStage.show();

            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeProgressDialog() {
        if (progressDialogStage != null) {
            progressDialogStage.close();
            progressDialogStage = null;
        }
    }

    public MainWindowController getMainWindowController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getResourceAsStream(MAIN_WINDOW_FXML_PATH));
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ProgressDialogController getProgressDialogController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getResourceAsStream(PROGRESS_DIALOG_FXML_PATH));
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}