//package com.private_void.app.progress_dialog;
//
//import javafx.stage.Stage;
//
//public class progress_dialog {
//    private final Stage stage;
//    private final ProgressBar pb = new ProgressBar();
//    private final ProgressIndicator pin = new ProgressIndicator();
//
//    public ProgressForm() {
//        dialogStage = new Stage();
//        dialogStage.initStyle(StageStyle.UTILITY);
//        dialogStage.setResizable(false);
//        dialogStage.initModality(Modality.APPLICATION_MODAL);
//
//        // PROGRESS BAR
//        final Label label = new Label();
//        label.setText("alerto");
//
//        pb.setProgress(-1F);
//        pin.setProgress(-1F);
//
//        final HBox hb = new HBox();
//        hb.setSpacing(5);
//        hb.setAlignment(Pos.CENTER);
//        hb.getChildren().addAll(pb, pin);
//
//        Scene scene = new Scene(hb);
//        dialogStage.setScene(scene);
//    }
//
//    public void activateProgressBar(final Task<?> task)  {
//        pb.progressProperty().bind(task.progressProperty());
//        pin.progressProperty().bind(task.progressProperty());
//        dialogStage.show();
//    }
//
//    public Stage getDialogStage() {
//        return dialogStage;
//    }
//}
