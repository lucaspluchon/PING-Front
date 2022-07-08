package com.app.ping.controller;

import com.app.ping.PingApp;
import javafx.util.Pair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SnippetSystem
{
    public static ArrayList<Pair<String, String>> getSnippets() throws URISyntaxException, IOException {
        ArrayList<Pair<String, String>> res = new ArrayList<>();
       JSONObject config = new JSONObject(PingApp.readRessource("snippets.json"));

        for (String key: config.keySet())
        {
            res.add(new Pair<>(key, config.getString(key)));
        }
        return res;
    }
}
