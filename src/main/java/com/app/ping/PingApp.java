package com.app.ping;

import com.app.ping.controller.*;
import com.app.ping.weather.WeatherManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

public class PingApp extends Application {
    public static File projectFolder = null;
    public static CodeArea actualEditor = null;
    public static File actualFile = null;

    public static Language language = null;
    public static String city = null;
    public static Scene _scene;


    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        copyRessource("style.css");
        Path path = Path.of(System.getProperty("user.home"),"config.json");
        if (!path.toFile().exists())
            path.toFile().createNewFile();

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

    public static void copyRessource(String name) throws IOException {
        String style = readRessource("style.css");
        File file = Path.of(System.getProperty("user.dir"),"style.css").toFile();
        file.createNewFile();
        try
        {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(style);
            fileWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String readRessource(String name) throws IOException {
        InputStream in = PingApp.class.getResourceAsStream(name);
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(in));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        StringBuilder res = new StringBuilder();
        String line;
        if (reader != null)
        {
            while ((line = reader.readLine()) != null)
            {
                res.append(line);
                res.append("\n");
            }
        }
        return res.toString();
    }

    public static void start() {
        launch();
    }

}