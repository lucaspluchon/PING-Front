package com.app.ping;

import com.app.ping.controller.*;
import com.app.ping.weather.WeatherManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class PingApp extends Application {
    public static File projectFolder = null;
    public static CodeArea actualEditor = null;
    public static File actualFile = null;

    public static Language language = null;
    public static String city = null;
    public static Scene _scene;


    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        language = LanguageSystem.getLanguage();
        city = WeatherManager.getWeatherConfig();


        FXMLLoader fxmlLoader = new FXMLLoader(PingApp.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Project Premier Gaou");
        stage.setScene(scene);
        stage.show();
        _scene = scene;
        LanguageSystem.load(scene);




        stage.setOnCloseRequest(event ->
        {
            Platform.exit();
            System.exit(0);
        });

        scene.getStylesheets().add(Controller.class.getResource("java-keywords.css").toExternalForm());
    }

    public static void start() {
        launch();
    }

}