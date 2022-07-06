package com.app.ping.controller;

import com.app.ping.Controller;
import com.app.ping.PingApp;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

public class TextIde
{
    private static long lastKeyPressed = 0;

    public static Tab newFileTab(TabPane codeTab, File file)
    {
        Tab exist = null;
        if (file != null)
        {
            for (var tab : codeTab.getTabs())
            {
                if (((FileInfo) tab.getUserData()).file() == null)
                    continue;
                if (((FileInfo) tab.getUserData()).file().toString().equals(file.toString()))
                    exist = tab;
            }
        }


        if (exist != null)
            return exist;

        String name = LanguageSystem.unamedFile;
        if (file != null)
            name = file.getName();

        CodeArea textEditor = new CodeArea();
        new SyntaxHighlighting(textEditor);
        new AutoComplete(textEditor);
        textEditor.setParagraphGraphicFactory(LineNumberFactory.get(textEditor));

        Tab newTab = new Tab(name, textEditor);
        newTab.setUserData(new FileInfo(file, textEditor));
        codeTab.getTabs().add(newTab);
        return newTab;
    }

    public static void saveActualFile() throws IOException
    {
        if (PingApp.actualFile != null)
        {
            BufferedWriter file = new BufferedWriter(new FileWriter(PingApp.actualFile));
            file.write(PingApp.actualEditor.getText());
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
        if (PingApp.actualFile == null || !PingApp.actualFile.getAbsolutePath().equals(file.getAbsolutePath()));
        {
            String content = Files.readString(file.toPath());
            setText(textEditor, content);
        }
    }



}
