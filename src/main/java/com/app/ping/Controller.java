package com.app.ping;

import com.app.ping.controller.CodeExecution;
import com.app.ping.controller.Menu;
import com.app.ping.controller.TextIde;
import javafx.concurrent.Task;
import com.app.ping.weather.WeatherManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.Console;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    @FXML public Button menuButton;
    @FXML public ContextMenu contextMenu;
    @FXML public CodeArea textEditor;
    @FXML public TextArea consoleResult;
    @FXML public BorderPane window;
    @FXML public TreeView<NodeClass> projectTree;

    private static final String[] KEYWORDS = new String[] {
            "min", "max", "compare", "garbage_collect", "case-sensitive",
            "is", "current_predicate", "catch", "throw", "No",
            "fail", "not", "true", "Yes", "forall",
            "member", "concat_atom", "last", "append", "module", "use_module",
            ":-"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\.";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "%[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    //private static final String FUNCTION_PATTERN = "([^\"\\\\]|\\\\.)*(([^\"\\\\]|\\\\.)*)";


    private ExecutorService executor;

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    //+ "|(?<FUNCTION>" + FUNCTION_PATTERN + ")"
    );

    public void initialize()
    {
        WeatherManager.setTimer(textEditor, consoleResult, projectTree, contextMenu);
        executor = Executors.newSingleThreadExecutor();
        Subscription cleanupWhenDone = textEditor.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .retainLatestUntilLater(executor)
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(textEditor.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

    }

    @FXML protected void updateTextIde() throws IOException {
        TextIde.update(textEditor);
    }

    @FXML protected void showMenu() { Menu.show(contextMenu, menuButton); }
    @FXML protected void openFile() throws IOException { Menu.openFile(menuButton, textEditor, projectTree); }
    @FXML protected void openFolder() throws IOException { Menu.openFolder(menuButton, projectTree, textEditor); }
    @FXML protected void executeCode() throws IOException, InterruptedException { CodeExecution.execute(consoleResult, textEditor, window); }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        textEditor.setStyleSpans(0, highlighting);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = textEditor.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

}
