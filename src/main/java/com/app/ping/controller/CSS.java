package com.app.ping.controller;

import com.app.ping.Controller;
import com.app.ping.PingApp;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSS {

    private static List<String> backup = null;
    private static Path path;

    static {
        path = Paths.get(Controller.class.getResource("style.css").getPath());
    }


    private static int getStyleLine(List<String> lines, String style)
    {
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).contains(style))
                return i + 2;
        }
        throw new RuntimeException();
    }

    private static void changeFile(String elm, String style) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        backup = new ArrayList<>(lines);
        int lineNumber = getStyleLine(lines, elm);
        lines.set(lineNumber, style);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public static void setStyle(Parent node, String elm, String style) throws IOException {
        if (node != null)
            node.getStylesheets().clear();
        changeFile(elm, style);
        if (node != null)
            node.getStylesheets().add(PingApp.class.getResource("style.css").toString());
    }
}
