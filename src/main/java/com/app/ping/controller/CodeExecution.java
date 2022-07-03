package com.app.ping.controller;

import com.app.ping.PingApp;
import com.app.ping.weather.WeatherManager;
import javafx.animation.RotateTransition;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Path;


public class CodeExecution
{

    private static boolean hasError(String stderr)
    {
        return stderr.contains("ERROR:");
    }

    private static int countError(String stderr, Path path)
    {
        return (stderr.split(path.toAbsolutePath().toString(), -1).length) - 1;
    }

    public static void execute(TextArea consoleResult, TabPane codeTab, BorderPane window, Tab tab) throws IOException, InterruptedException
    {
        if (PingApp.actualFile == null)
        {
            Dialog.error("Premier gaou Error", "Cannot run this file", "No file opened");
            return;
        }
        TextIde.saveActualFile();
        String[] cmd = {"swipl", "-s", PingApp.actualFile.getAbsolutePath(), "-t" ,"halt.", "-q"};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(PingApp.actualFile.getParentFile());
        Process process = pb.start();
        process.waitFor();
        String stdout = new String(process.getInputStream().readAllBytes());
        String stderr = new String(process.getErrorStream().readAllBytes());
        codeTab.getSelectionModel().select(tab);

        if (!hasError(stderr))
        {
            consoleResult.setStyle(String.format("-fx-control-inner-background: '%s'; -fx-background-color: '%s'; -fx-text-fill: '%s'", WeatherManager.backColor, WeatherManager.backColor, WeatherManager.textColor));
            consoleResult.setText(stdout);
        }
        else
        {
            consoleResult.setStyle(String.format("-fx-control-inner-background: '%s'; -fx-background-color: '%s'; -fx-text-fill: '%s'", WeatherManager.backColor, WeatherManager.backColor, "red"));
            showError(window, countError(stderr, PingApp.actualFile.toPath()));
            consoleResult.setText(stderr);
        }


    }

    private static void showError(BorderPane window, int count)
    {
        RotateTransition anim = new RotateTransition(Duration.millis(800));
        anim.setNode(window);
        anim.setByAngle(360);
        anim.setCycleCount(50);
        anim.setCycleCount(count);
        anim.play();
    }
}
