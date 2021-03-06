package com.app.ping.controller;

import com.app.ping.Controller;
import com.app.ping.PingApp;
import javafx.scene.Parent;

import java.io.IOException;
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
        path = Path.of(System.getProperty("user.dir"),"style.css");
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
            node.getStylesheets().add("file:style.css");
    }
}
