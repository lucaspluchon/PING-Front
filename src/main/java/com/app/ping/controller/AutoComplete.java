package com.app.ping.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.Subscription;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.LongUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AutoComplete
{
    private static final String[] KEYWORDS = new String[]{
            "min", "max", "compare", "garbage_collect", "case-sensitive",
            "is", "current_predicate", "catch", "throw", "No",
            "fail", "not", "true", "Yes", "forall",
            "member", "concat_atom", "last", "append", "module", "use_module",
            ":-", "initialization", "main"
    };

    private static ArrayList<Pair<String, String>> snippets = new ArrayList<Pair<String, String>>();


    private final CodeArea textEditor;
    private ExecutorService executor;
    private ContextMenu menu = new ContextMenu();
    private Integer wordLen = 0;
    public Boolean isActive = true;


    public AutoComplete(CodeArea textEditor) throws URISyntaxException, IOException {
        this.textEditor = textEditor;
        this.executor = Executors.newSingleThreadExecutor();
        setSnippets();
        Subscription cleanupWhenDone = textEditor.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .retainLatestUntilLater(executor)
                .supplyTask(this::getAutocompletAsync)
                .awaitLatest(textEditor.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::deployAutocomplete);
        textEditor.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
        {
            if (menu.isShowing())
                menu.hide();
            if (KE.getCode() == KeyCode.UP
                    || KE.getCode() == KeyCode.DOWN
                    || KE.getCode() == KeyCode.LEFT
                    || KE.getCode() == KeyCode.RIGHT
                    || KE.getCode() == KeyCode.BACK_SPACE
                    || KE.getCode() == KeyCode.DELETE
            )
            {

                isActive = false;
            }
            else
            {
                isActive = true;
            }
        });
        menu.setOnShown(e ->
        {
            menu.requestFocus();
        });
    }

    private void setSnippets() throws URISyntaxException, IOException {
        snippets = SnippetSystem.getSnippets();
    }

    public List<Pair<String, String>> getAutocomplet(String text)
    {
        text = text.substring(0, textEditor.getCaretPosition());
        if (!isActive
                || text.length() <= 0
                || !("" + text.charAt(text.length() - 1)).matches("\\w"))
        {
            return new ArrayList<>();
        }
        String[] splited = text.split("\\W");
        String word = splited[splited.length - 1];
        this.wordLen = word.length();

        ArrayList<Pair<Pair<String, String>, Integer>> res = new ArrayList<>();
        for (String keyword : KEYWORDS)
        {
            int distance = LevenshteinDistance
                    .getDefaultInstance()
                    .apply(
                            word.toLowerCase(Locale.ROOT),
                            keyword.toLowerCase(Locale.ROOT)
                    );
            if (keyword.toLowerCase(Locale.ROOT).startsWith(word.toLowerCase(Locale.ROOT)))
                distance = 0;
            Pair<Pair<String, String>, Integer> pair = new Pair<>(
                    new Pair<>(keyword, keyword),
                    distance
            );
            res.add(pair);
        }
        for (Pair<String, String> snippet : snippets)
        {
            int distance = LevenshteinDistance
                    .getDefaultInstance()
                    .apply(
                            word.toLowerCase(Locale.ROOT),
                            snippet.getKey().toLowerCase(Locale.ROOT)
                    );
            if (snippet.getKey().toLowerCase(Locale.ROOT).startsWith(word.toLowerCase(Locale.ROOT)))
                distance = 0;
            Pair<Pair<String, String>, Integer> pair =
                    new Pair<>(
                            snippet,
                            distance
                    );
            res.add(pair);
        }
        ArrayList<Pair<String, String>> selected = new ArrayList<>();
        res
                .stream()
                .filter(p -> p.getValue() <= 2)
                .sorted(Comparator.comparingInt(Pair::getValue))
                .forEach(p -> selected.add(p.getKey()));
        return selected;
    }

    private Task<List<Pair<String, String>>> getAutocompletAsync()
    {
        String text = textEditor.getText();
        Task<List<Pair<String, String>>> task = new Task<List<Pair<String, String>>>()
        {
            @Override
            protected List<Pair<String, String>> call() throws Exception
            {
                return getAutocomplet(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void deployAutocomplete(List<Pair<String, String>> words)
    {
        if (menu.isShowing())
            this.menu.hide();
        if (words.isEmpty())
            return;
        menu = new ContextMenu();
        menu.getItems().clear();
        for (Pair<String, String> word : words)
        {
            MenuItem menuItem = new MenuItem(word.getKey());
            menuItem.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent)
                {
                    int position = textEditor.getCaretPosition();
                    textEditor.deleteText(position - wordLen, position);
                    textEditor.insertText(position - wordLen, word.getValue());
                }
            });
            menu.getItems().add(menuItem);
        }
        Optional<Bounds> postion = textEditor.getCaretBounds();
        if (postion.isEmpty())
            return;
        menu.show(
                textEditor.getScene().getWindow(),
                postion.get().getCenterX(),
                postion.get().getCenterY()
        );

    }
}
