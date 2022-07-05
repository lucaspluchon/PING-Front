package com.app.ping.controller;

import com.app.ping.Language;
import com.app.ping.PingApp;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.app.ping.Controller._contextMenu;

public class LanguageSystem
{
    public static String unamedFile = "Unnamed file";
    public static Language getLanguage() throws IOException
    {
        Path path = Path.of(System.getProperty("user.dir"), "src/main/resources/com/app/ping", "config.json");
        if (!Files.exists(path))
            return null;
        JSONObject config;
        try
        {
            config = new JSONObject(Files.readString(path));
        }
        catch (Exception e)
        {
            return Language.English;
        }

        return Language.valueOf(config.getString("language"));
    }

    public static void load(Scene window) throws IOException {
         String file = null;
        if (PingApp.language == null || PingApp.language == Language.English)
            file = "english.json";
        else if (PingApp.language == Language.Indian)
            file = "indian.json";
        else if (PingApp.language == Language.Greek)
            file = "greek.json";

        JSONObject config = new JSONObject(Files.readString(Path.of(System.getProperty("user.dir"), "src/main/resources/com/app/ping", file)));
        reloadText(config, window) ;
    }

    public static void reloadText(JSONObject config, Scene window)
    {
        ((Button) window.lookup("#menuButton")).setText(config.getString("menuButton"));
        ((TabPane) window.lookup("#resultTab")).getTabs().get(0).setText(config.getString("resultTab"));
        ((TabPane) window.lookup("#resultTab")).getTabs().get(1).setText(config.getString("terminalTab"));
        _contextMenu.getItems().get(0).setText(config.getString("createFile"));
        _contextMenu.getItems().get(1).setText(config.getString("openFile"));
        _contextMenu.getItems().get(2).setText(config.getString("openProject"));
        _contextMenu.getItems().get(3).setText(config.getString("runFile"));
        _contextMenu.getItems().get(4).setText(config.getString("saveFile"));
        unamedFile = config.getString("unnamedFile");
    }
}
