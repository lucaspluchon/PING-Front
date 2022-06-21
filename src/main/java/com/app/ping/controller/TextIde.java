package com.app.ping.controller;

import com.app.ping.Controller;
import com.app.ping.PingApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class TextIde
{
    private static long lastKeyPressed = 0;

    public static void saveFile(TextArea textEditor) throws IOException
    {
        if (PingApp.rootPath != null)
        {
            BufferedWriter file = new BufferedWriter(new FileWriter(PingApp.rootPath.toFile()));
            file.write(textEditor.getText());
            file.close();
        }
    }

    public static void update(TextArea textEditor) throws IOException
    {
        long actualTime = Instant.now().getEpochSecond();
        if (actualTime - lastKeyPressed >= 2)
        {
            saveFile(textEditor);
        }
        lastKeyPressed = Instant.now().getEpochSecond();
    }
}
