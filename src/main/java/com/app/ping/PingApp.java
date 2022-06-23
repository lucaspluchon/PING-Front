package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import com.app.ping.weather.WeatherManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class PingApp extends Application {
    public static Path rootPath = null;
    public static Path actualPath = null;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PingApp.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Project Premier Gaou");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}