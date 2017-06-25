package com.private_void.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//TODO на графике показывается расположение координат частиц, а не интенсивность на детекторе, так что нужно с этим разобраться
//TODO возможно убрать вкладки и просто оставить одни и те же поля для ввода параметров, но в зависимости от выбранного типа капилляра и пучка изменять текст лейбелов и видимость лишних полей
//TODO обработать случай с тупым пользователем и некорректным вводом в поля. Также подумать о безопасности кастования в целом
//TODO разобраться с самой логикой, там что-то считается неправильно
//TODO выводить статистику по частицам в соответствующие лейблы
//TODO доделать стилизацию графика

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/FXML/MainScene.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));

        stage.setTitle("CapStruct");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(0, "styles/styles.css");
        stage.setScene(scene);
        stage.show();
    }
}