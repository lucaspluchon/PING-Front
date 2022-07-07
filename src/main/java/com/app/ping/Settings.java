package com.app.ping;

import com.app.ping.controller.LanguageSystem;
import com.app.ping.controller.Menu;
import com.app.ping.weather.WeatherManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Settings
{
    @FXML public Button okButton;
    @FXML public ComboBox<String> languageCombo;
    @FXML public TextField locationText;
    @FXML public Label language;
    @FXML public Label locationLabel;
    @FXML public Label settings;

    @FXML public void initialize() throws IOException {
        languageCombo.getItems().add(Language.English.value());
        languageCombo.getItems().add(Language.Indian.value());
        languageCombo.getItems().add(Language.Greek.value());
        languageCombo.getSelectionModel().select(Objects.requireNonNull(LanguageSystem.getLanguage()).value());
        String city = WeatherManager.getWeatherConfig();
        System.out.println(city);
        locationText.setText(city != null ? city: "");
        reload();
    }

    private void reload()
    {
        settings.setText(LanguageSystem.config.getString("settings"));
        locationLabel.setText(LanguageSystem.config.getString("location"));
        language.setText(LanguageSystem.config.getString("language"));
        okButton.setText(LanguageSystem.config.getString("done"));
    }

    @FXML protected void changeLanguage() throws IOException {
        if (Objects.equals(languageCombo.getSelectionModel().getSelectedItem(), Language.English.value()))
            PingApp.language = Language.English;
        if (Objects.equals(languageCombo.getSelectionModel().getSelectedItem(), Language.Greek.value()))
            PingApp.language = Language.Greek;
        if (Objects.equals(languageCombo.getSelectionModel().getSelectedItem(), Language.Indian.value()))
            PingApp.language = Language.Indian;
        LanguageSystem.load(PingApp._scene);

        Path path = Path.of(System.getProperty("user.dir"), "src/main/resources/com/app/ping", "config.json");
        JSONObject config;
        try
        {
            config = new JSONObject(path);
        }
        catch (Exception e)
        {
            config = new JSONObject();
        }
        config.put("language", PingApp.language.toString());
        config.put("city", PingApp.city);
        FileWriter myWriter = new FileWriter(path.toString());
        myWriter.write(config.toString());
        myWriter.close();
        reload();
    }

    @FXML protected void closeWindow() throws IOException {

        PingApp.city = locationText.getText();
        Path path = Path.of(System.getProperty("user.dir"), "src/main/resources/com/app/ping", "config.json");
        JSONObject config;
        try
        {
            config = new JSONObject(path);
        }
        catch (Exception e)
        {
            config = new JSONObject();
        }
        config.put("language", PingApp.language.toString());
        String newCity = locationText.getText();
        if (!Objects.equals(newCity, ""))
        {
            config.put("city", PingApp.city);
        }
        else
        {
            PingApp.city = null;
        }
        FileWriter myWriter = new FileWriter(path.toString());
        myWriter.write(config.toString());
        myWriter.close();

        if (WeatherManager.timer != null)
            WeatherManager.timer.cancel();
        WeatherManager.startTimer();
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
