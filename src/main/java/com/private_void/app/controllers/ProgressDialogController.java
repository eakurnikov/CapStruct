package com.private_void.app.controllers;

import com.private_void.core.detection.Distribution;
import com.private_void.utils.notifiers.Logger;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

public class ProgressDialogController extends CapStructController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea progressTextArea;

    @FXML
    private void initialize() {
        Logger.setOutputResource(progressTextArea);
    }

    public void bindProgressBar(Service<Distribution> calculationService) {
        progressBar.progressProperty().bind(calculationService.progressProperty());
    }

    public void unbindProgressBar() {
        progressBar.progressProperty().unbind();
    }
}