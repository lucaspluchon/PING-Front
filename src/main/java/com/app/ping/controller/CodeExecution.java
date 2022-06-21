package com.app.ping.controller;

import com.app.ping.PingApp;
import javafx.animation.RotateTransition;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.fxmisc.richtext.StyledTextArea;

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

    public static void execute(TextArea consoleResult, StyledTextArea textEditor, BorderPane window) throws IOException, InterruptedException
    {
        TextIde.saveFile(textEditor);
        String[] cmd = {"swipl", "-s", PingApp.actualPath.toAbsolutePath().toString(), "-t" ,"halt.", "-q"};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(PingApp.actualPath.toAbsolutePath().getParent().toFile());
        Process process = pb.start();
        process.waitFor();
        String stdout = new String(process.getInputStream().readAllBytes());
        String stderr = new String(process.getErrorStream().readAllBytes());

        if (!hasError(stderr))
        {
            consoleResult.setStyle("");
            consoleResult.setText(stdout);
        }
        else
        {
            consoleResult.setStyle("-fx-text-fill: red ;");
            showError(window, countError(stderr, PingApp.rootPath));
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
