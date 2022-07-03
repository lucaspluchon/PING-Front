package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import com.app.ping.weather.WeatherManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PingApp extends Application {
    public static File projectFolder = null;
    public static CodeArea actualEditor = null;
    public static File actualFile = null;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PingApp.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Project Premier Gaou");
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest(event ->
        {
            Platform.exit();
            System.exit(0);
        });

        scene.getStylesheets().add(Controller.class.getResource("java-keywords.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }

}