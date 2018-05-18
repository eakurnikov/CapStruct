package com.private_void.app.progress_dialog;

import com.private_void.app.CapStructController;
import com.private_void.core.detectors.Distribution;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressDialogController extends CapStructController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private void initialize() {}

    public void bind(Service<Distribution> service) {
        progressBar.progressProperty().bind(service.progressProperty());
        progressLabel.textProperty().bind(service.messageProperty());
    }

    public void unbind() {
        progressBar.progressProperty().unbind();
        progressLabel.textProperty().unbind();
    }
}