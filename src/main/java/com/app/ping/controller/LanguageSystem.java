package com.app.ping.controller;

import com.app.ping.Language;
import com.app.ping.PingApp;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.app.ping.Controller._contextMenu;

public class LanguageSystem
{
    public static String unamedFile = "Unnamed file";
    public static JSONObject config = null;


    public static Language getLanguage() throws IOException, URISyntaxException {
        Path path = null;
        try
        {
            path = Path.of(System.getProperty("user.home"),"config.json");
            if (!path.toFile().exists())
                return Language.English;
        }
        catch (Exception e)
        {
            return Language.English;
        }

        if (!Files.exists(path))
            return Language.English;
        JSONObject config;
        try
        {
            config = new JSONObject(Files.readString(path));
        }
        catch (Exception e)
        {
            return Language.English;
        }

        try {
            return Language.valueOf(config.getString("language"));
        }
        catch (JSONException e)
        {
            return Language.English;
        }
    }

    public static void load(Scene window) throws IOException, URISyntaxException {
        String file = null;
        if (PingApp.language == null || PingApp.language == Language.English)
            file = "english.json";
        else if (PingApp.language == Language.Indian)
            file = "indian.json";
        else if (PingApp.language == Language.Greek)
            file = "greek.json";

        config = new JSONObject(PingApp.readRessource(file));
        reloadText(window) ;
    }

    public static void reloadText(Scene window)
    {
        ((Button) window.lookup("#menuButton")).setText(config.getString("menuButton"));
        ((TabPane) window.lookup("#resultTab")).getTabs().get(0).setText(config.getString("resultTab"));
        ((TabPane) window.lookup("#resultTab")).getTabs().get(1).setText(config.getString("terminalTab"));
        _contextMenu.getItems().get(0).setText(config.getString("createFile"));
        _contextMenu.getItems().get(1).setText(config.getString("openFile"));
        _contextMenu.getItems().get(2).setText(config.getString("openProject"));
        _contextMenu.getItems().get(3).setText(config.getString("runFile"));
        _contextMenu.getItems().get(4).setText(config.getString("saveFile"));
        _contextMenu.getItems().get(5).setText(config.getString("settings"));
        _contextMenu.getItems().get(6).setText(config.getString("documentation"));
        unamedFile = config.getString("unnamedFile");
    }
}
