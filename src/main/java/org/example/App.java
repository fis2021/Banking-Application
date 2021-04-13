package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("dashboardScreen"), 1280, 720);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void changeScene(Button source, String fxml_file) throws IOException {
        ((Stage) source.getScene().getWindow()).setScene(new Scene(loadFXML(fxml_file), 1280, 720));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml")).load();
    }

    public static void main(String[] args) { launch(); }

}