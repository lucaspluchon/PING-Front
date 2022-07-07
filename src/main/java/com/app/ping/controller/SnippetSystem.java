package com.app.ping.controller;

import com.app.ping.PingApp;
import javafx.util.Pair;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SnippetSystem
{
    public static ArrayList<Pair<String, String>> getSnippets() throws URISyntaxException {
        ArrayList<Pair<String, String>> res = new ArrayList<>();
        Path path = Path.of(PingApp.class.getResource("snippets.json").getPath());
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
