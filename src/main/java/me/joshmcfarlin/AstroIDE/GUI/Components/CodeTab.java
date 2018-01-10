package main.java.me.joshmcfarlin.AstroIDE.GUI.Components;

import main.java.me.joshmcfarlin.AstroIDE.Utils.Permissions;
import javafx.scene.control.SplitPane;
import java.io.File;
import java.io.IOException;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CodeTab extends CustomTab {
    public CodeEditor codeEditor;
    private SplitPane splitPane = new SplitPane();
    private WebView webView = new WebView();

    CodeTab() {
        setText("New File");
        getStyleClass().add("tabDesign");
        setUserData(null);
        codeEditor = new CodeEditor();
        setup();
    }

    CodeTab(File file) {
        setText(file.getName());
        getStyleClass().add("tabDesign");
        setUserData(file);
        try {
            codeEditor = new CodeEditor(file);
        } catch (IOException e) {
            codeEditor = new CodeEditor();
        }
        setup();
    }

    private void setup() {
        setClosable(true);
        codeEditor.setPrefHeight(836);
        codeEditor.setPrefWidth(1018);
        codeEditor.getStyleClass().add("tabTextArea");
        splitPane.getItems().add(codeEditor);
        setContent(splitPane);
    }

    public void toggleHTMLView() {
        if (!splitPane.getItems().contains(webView)) {
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(codeEditor.getText());
            splitPane.getItems().add(webView);
        } else {
            splitPane.getItems().remove(webView);
        }
    }

    @Override
    public boolean Enabled(Permissions p) {
        return true;
    }
}