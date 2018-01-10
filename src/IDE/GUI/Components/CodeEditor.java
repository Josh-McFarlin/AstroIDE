package IDE.GUI.Components;

import javafx.scene.control.SplitPane;
import javafx.scene.web.WebView;
import java.awt.*;
import java.util.List;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class CodeEditor extends SplitPane {
    private WebView webView = new WebView();
    private URL htmlUrl = getClass().getResource("/IDE/GUI/Resources/CodeEditor/CodeEditor.html");
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private boolean searching;
    private int wrapLimit;

    public static String[] modes = {"ABAP", "ABC", "ActionScript", "ADA", "Apache_Conf", "AsciiDoc", "Assembly_x86",
            "AutoHotKey", "BatchFile", "C9Search", "C_Cpp", "Cirru", "Clojure", "Cobol", "coffee", "ColdFusion",
            "CSharp", "CSS", "Curly", "D", "Dart", "Diff", "Dockerfile", "Dot", "Dummy", "DummySyntax", "Eiffel",
            "EJS", "Elixir", "Elm", "Erlang", "Forth", "FTL", "Gcode", "Gherkin", "Gitignore", "Glsl", "golang",
            "Groovy", "HAML", "Handlebars", "Haskell", "haXe", "HTML", "HTML_Ruby", "INI", "Io", "Jack", "Jade",
            "Java", "JavaScript", "JSON", "JSONiq", "JSP", "JSX", "Julia", "LaTeX", "LESS", "Liquid", "Lisp",
            "LiveScript", "LogiQL", "LSL", "Lua", "LuaPage", "Lucene", "Makefile", "Markdown", "Mask", "MATLAB",
            "MEL", "MUSHCode", "MySQL", "Nix", "ObjectiveC", "OCaml", "Pascal", "Perl", "pgSQL", "PHP", "Powershell",
            "Praat", "Prolog", "Properties", "Protobuf", "Python", "R", "RDoc", "RHTML", "Ruby", "Rust", "SASS",
            "SCAD", "Scala", "Scheme", "SCSS", "SH", "SJS", "Smarty", "snippets", "Soy_Template", "Space", "SQL",
            "Stylus", "SVG", "Tcl", "Tex", "Text", "Textile", "Toml", "Twig", "Typescript", "Vala", "VBScript",
            "Velocity", "Verilog", "VHDL", "XML", "XQuery", "YAML"};

    public static String[] themes = {"ambiance", "chaos", "chrome", "clouds", "clouds_midnight", "cobalt",
            "crimson_editor", "dawn", "dracula", "dreamweaver", "eclipse", "github", "gob", "gruvbox",
            "idle_fingers", "iplastic", "katzenmilch", "kr_theme", "kuroir", "merbivore", "merbivore_soft",
            "mono_industrial", "monokai", "pastel_on_dark", "solarized_dark", "solarized_light", "sqlserver",
            "terminal", "textmate", "tomorrow", "tomorrow_night", "tomorrow_night_blue", "tomorrow_night_bright",
            "tomorrow_night_eighties", "twilight", "vibrant_ink", "xcode"};

    CodeEditor() {
        webView.getEngine().load(htmlUrl.toExternalForm());
        getItems().add(webView);
    }

    CodeEditor(File file) throws IOException {
        webView.getEngine().load(htmlUrl.toExternalForm());
        List<String> s = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        setText(String.join(" \\\n", s));
        getItems().add(webView);
    }

    public String getText() {
        return (String) webView.getEngine().executeScript("editor.getValue();");
    }

    public void setText(String newText) {
        newText = newText.replaceAll("\"", "\\\\\"");
        newText = newText.replaceAll("'","\\\\\'");
        System.out.println(newText);
        webView.getEngine().executeScript("setText('" + newText + "');");
    }

    public boolean setMode(String mode) {
        if (Arrays.asList(modes).contains(mode)) {
            webView.getEngine().executeScript("editor.getSession().setMode('ace/mode/" + mode.toLowerCase() + "');");
            return true;
        }
        return false;
    }

    public boolean setTheme(String theme) {
        if (Arrays.asList(themes).contains(theme)) {
            webView.getEngine().executeScript("editor.setTheme('ace/theme/" + theme + "');");
            return true;
        }
        return false;
    }

    public void setTabSize(int size) {
        webView.getEngine().executeScript("editor.getSession().setTabSize(" + size + ");");
    }

    public void setFontSize(int size) {
        webView.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
    }

    public void setWordWrapping(boolean b) {
        webView.getEngine().executeScript("editor.getSession().setUseWrapMode(" + b + ");");
    }

    public void setWrapLimit(int limit) {
        webView.getEngine().executeScript("editor.getSession().setUseWrapMode(" + limit + ");");
    }

    public void goToLine(int lineNum) {
        webView.getEngine().executeScript("editor.gotoLine(" + lineNum + ");");
    }

    public void cut() {
        String content = (String) webView.getEngine().executeScript("editor.session.getTextRange(editor.getSelectionRange());");
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, selection);
    }

    public void copy() {
        String content = (String) webView.getEngine().executeScript("editor.session.getTextRange(editor.getSelectionRange());");
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, selection);
    }

    public void paste() {
        try {
            String content = (String) clipboard.getData(DataFlavor.stringFlavor);
            webView.getEngine().executeScript("editor.insert('" + content + "');");
        } catch (IOException | UnsupportedFlavorException e) {
            System.out.println("Clipboard contents could not be accessed.");
        }
    }

    public void selectAll() {
        webView.getEngine().executeScript("editor.selectAll();");
    }

    public void deselect() {
        webView.getEngine().executeScript("editor.selection.clearSelection();");
    }

    public void undo() {
        webView.getEngine().executeScript("editor.undo();");
        findValue("var x", false, false, false, false, false);
    }

    public void redo() {
        webView.getEngine().executeScript("editor.redo();");
    }

    public void findValue(String find, boolean backwards, boolean wrap, boolean caseSens, boolean wholeWord, boolean regExp) {
        String command = String.format("editor.find('%s',{\n" +
                "    backwards: %s,\n" +
                "    wrap: %s,\n" +
                "    caseSensitive: %s,\n" +
                "    wholeWord: %s,\n" +
                "    regExp: %s\n" +
                "});\n" +
                "editor.findNext();\n" +
                "editor.findPrevious();\n", find, backwards, wrap, caseSens, wholeWord, regExp);
        webView.getEngine().executeScript(command);
        searching = true;
    }

    public void replaceWith(String replace) {
        if (searching) {
            webView.getEngine().executeScript("editor.replace('" + replace + "');");
            searching = false;
        }
    }

    public void replaceAllWith(String replace) {
        if (searching) {
            webView.getEngine().executeScript("editor.replaceAll('" + replace + "');");
            searching = false;
        }
    }

    public static void main(String[] args) {
        System.out.println(String.join("\n* ", modes));
    }
}