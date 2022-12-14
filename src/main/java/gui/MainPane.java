package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPane extends Application {
    public static void openWindow() {
        launch();
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPane.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Notenverwaltung");
        Image icon = new Image("file:src/main/resources/data/logo.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}