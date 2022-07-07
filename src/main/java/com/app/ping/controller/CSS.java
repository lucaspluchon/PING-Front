package com.app.ping.controller;

import com.app.ping.Controller;
import javafx.scene.Node;
import javafx.scene.Parent;

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
        try {
            path = Paths.get(Controller.class.getResource("style.css").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

    public static void setStyle(Parent node, String elm, String style) throws IOException {
        if (node != null)
            node.getStylesheets().clear();
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        backup = new ArrayList<>(lines);
        int lineNumber = getStyleLine(lines, elm);
        lines.set(lineNumber, style);
        Files.write(path, lines, StandardCharsets.UTF_8);
        if (node != null)
            node.getStylesheets().add("style.css");
    }
}
