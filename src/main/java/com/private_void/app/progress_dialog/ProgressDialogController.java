package com.private_void.app.progress_dialog;

import com.private_void.app.CapStructController;
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

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getProgressLabel() {
        return progressLabel;
    }
}