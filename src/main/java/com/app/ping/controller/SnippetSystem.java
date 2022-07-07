package com.app.ping.controller;

import com.app.ping.Language;
import javafx.util.Pair;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SnippetSystem
{
    public static ArrayList<Pair<String, String>> getSnippets()
    {
        ArrayList<Pair<String, String>> res = new ArrayList<>();
        Path path = Path.of(System.getProperty("user.dir"), "src/main/resources/com/app/ping", "snippets.json");
        if (!Files.exists(path))
            return null;
        JSONObject config;
        try
        {
            config = new JSONObject(Files.readString(path));
        }
        catch (Exception e)
        {
            return res;
        }

        for (String key: config.keySet())
        {
            res.add(new Pair<>(key, config.getString(key)));
        }
        return res;
    }
}
