package com.app.ping.controller;

import com.app.ping.Controller;
import com.app.ping.PingApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

public class TextIde
{
    private static long lastKeyPressed = 0;

    public static void saveFile(CodeArea textEditor) throws IOException
    {
        if (PingApp.actualPath != null)
        {
            BufferedWriter file = new BufferedWriter(new FileWriter(PingApp.actualPath.toFile()));
            file.write(textEditor.getText());
            file.close();
        }
    }

    public static void setText(CodeArea textEditor, String content)
    {
        textEditor.deleteText(0,textEditor.getLength());
        textEditor.appendText(content);
    }

    public static void readFile(CodeArea textEditor, File file) throws IOException
    {
        if (PingApp.actualPath == null || !PingApp.actualPath.toAbsolutePath().toString().equals(file.toPath().toAbsolutePath().toString()))
        {
            textEditor.setEditable(true);
            saveFile(textEditor);
            PingApp.actualPath = file.toPath();
            String content = Files.readString(file.toPath());
            setText(textEditor, content);
        }
    }

    public static void update(CodeArea textEditor) throws IOException
    {
        long actualTime = Instant.now().getEpochSecond();
        if (actualTime - lastKeyPressed >= 2)
        {
            saveFile(textEditor);
        }
        lastKeyPressed = Instant.now().getEpochSecond();
    }


}
