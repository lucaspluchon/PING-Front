package com.app.ping.controller;

import com.app.ping.NodeClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Tree {

    public static ArrayList<TreeItem<NodeClass>> listItem = new ArrayList<>();
    public static void initFile(TreeView<NodeClass> tree, Path file)
    {
        TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FILE, file));
        tree.setRoot(elm);
        listItem.add(elm);
    }

    public static void initFolder(TreeView<NodeClass> tree, File folder, CodeArea textIde)
    {
        TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FOLDER, folder.toPath()));
        tree.setRoot(elm);
        listItem.add(elm);

        tree.setOnMouseClicked(mouseEvent -> {
            TreeItem<NodeClass> node = tree.getSelectionModel().getSelectedItem();
            if (node != null && node.getValue().type == NodeType.FILE)
            {
                try
                {
                    TextIde.readFile(textIde, node.getValue().path.toFile());
                }
                catch (Exception ignored) {}
            }
        });

        tree.getRoot().setExpanded(true);
        populateProject(tree.getRoot(), folder);
    }

    public static void populateProject(TreeItem<NodeClass> root, File folder)
    {
        for (File file : Objects.requireNonNull(folder.listFiles()))
        {
            if (file.isDirectory())
            {
                int index = root.getChildren().size();
                TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FOLDER, file.toPath()));
                root.getChildren().add(elm);
                listItem.add(elm);
                populateProject(root.getChildren().get(index), file);
            }
            else
            {
                TreeItem<NodeClass> elm = new TreeItem<>(new NodeClass(NodeType.FOLDER, file.toPath()));
                listItem.add(elm);
                root.getChildren().add(elm);
            }
        }
    }
}
