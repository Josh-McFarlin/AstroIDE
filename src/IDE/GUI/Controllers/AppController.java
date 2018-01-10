package IDE.GUI.Controllers;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import IDE.GUI.Components.*;
import IDE.Utils.ImageSelection;
import IDE.Utils.Permissions;
import IDE.Utils.RecentFileList;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import java.nio.file.Files;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.nio.file.StandardOpenOption;
import java.io.*;

public class AppController implements Serializable {

    @FXML
    private VBox vBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu openRecentDrop;

    @FXML
    private FileTree fileTree;

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane treeAnchor;

    @FXML
    private IDEPane tabPane;

    @FXML
    private Menu syntaxModeDrop;

    // Vars
    private Image icon = new Image(getClass().getResourceAsStream("/IDE/GUI/Resources/Images/folder.png"));

    private File currentFile;

    // End Vars

    @FXML
    void createTab() {
        tabPane.createTab();
    }

    private boolean isImage(File file) {
        if (file != null) {
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            return Arrays.asList(ImageIO.getWriterFormatNames()).contains(ext);
        } else {
            return false;
        }
    }

    @FXML
    void closeTab() {
        tabPane.closeTab();
    }

    @FXML
    void openAboutPage() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
        Stage stage = new Stage();
        stage.setTitle("About");
        Scene scene = new Scene(root, 400, 225);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void openSettings() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Preferences");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void quitApp() {
        Platform.exit();
    }

    @FXML
    void cutAction() {
        if (tabPane.checkTabAccess(Permissions.CUT) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().cut();
        }
    }

    @FXML
    void copyAction() {
        if (tabPane.checkTabAccess(Permissions.COPY)) {
            if (!isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
                tabPane.getCodeEditor().copy();
            } else {
                ImageSelection imgSel = new ImageSelection(tabPane.getImage());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
            }
        }
    }

    @FXML
    void pasteAction() {
        if (tabPane.checkTabAccess(Permissions.PASTE) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().paste();
        }
    }

    @FXML
    void selectAllAction() {
        if (tabPane.checkTabAccess(Permissions.SELECTALL) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().selectAll();
        }
    }

    @FXML
    void deselectAction() {
        if (tabPane.checkTabAccess(Permissions.DESELECT) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().deselect();
        }
    }

    @FXML
    void compileAction() throws Exception {
        if (tabPane.checkTabAccess(Permissions.COMPILE) && tabPane.getSelectionModel().getSelectedItem() instanceof CodeTab) {
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            File file = (File) tab.getUserData();

            // Add compile code
        }
    }

    @FXML
    void runAction() throws Exception {
        if (tabPane.checkTabAccess(Permissions.RUN) && tabPane.getSelectionModel().getSelectedItem() instanceof CodeTab) {
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            File file = (File) tab.getUserData();

            // Add run code
        }
    }

    @FXML
    void toggleHTMLAction() {
        if (tabPane.checkTabAccess(Permissions.HTML) && tabPane.getSelectionModel().getSelectedItem() instanceof CodeTab) {
            ((CodeTab) tabPane.getSelectionModel().getSelectedItem()).toggleHTMLView();
        }
    }

    @FXML
    void undoFile() {
        if (tabPane.checkTabAccess(Permissions.UNDO) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().undo();
        }
    }

    @FXML
    void redoFile() {
        if (tabPane.checkTabAccess(Permissions.REDO) && !isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) {
            tabPane.getCodeEditor().redo();
        }
    }

    @FXML
    void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(((FileItem) fileTree.getRoot()).getFile());
        fileChooser.setTitle("Open File");
        Stage stage = (Stage) vBox.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null && file.exists()) {
            tabPane.createTab(file);
        }
    }

    @FXML
    void openDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(((FileItem) fileTree.getRoot()).getFile());
        directoryChooser.setTitle("Open Directory");
        Stage stage = (Stage) vBox.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);
        if (file != null && file.exists()) {
            fileTree.setRoot(file);
        }
    }

    @FXML
    void saveFile() throws IOException {
        currentFile = (File) tabPane.getSelectionModel().getSelectedItem().getUserData();

        if (currentFile.exists()) {
            if (!isImage(currentFile)) { // code file
                OutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(currentFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE));
                out.write(tabPane.getCode().getBytes(), 0, tabPane.getCode().length());
                RecentFileList.addRecent(currentFile);
            } else { // image file
                saveFileAs();
            }
        } else {
            saveFileAs();
        }
    }

    @FXML
    void saveFileAs() throws IOException {
        Stage stage = (Stage) vBox.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(tabPane.getSelectionModel().getSelectedItem().getText());

        currentFile = (File) tabPane.getSelectionModel().getSelectedItem().getUserData();
        if (currentFile.exists()) {
            fileChooser.setInitialDirectory(currentFile.getParentFile());
            fileChooser.setInitialFileName(currentFile.getName());
        }
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            if (!isImage((File) tabPane.getSelectionModel().getSelectedItem().getUserData())) { // code file
                OutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE));
                out.write(tabPane.getCode().getBytes(), 0, tabPane.getCode().length());
            } else { // image file
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                ImageIO.write(tabPane.getImage(), ext, file);
            }
            RecentFileList.addRecent(file);
        }
    }

    @FXML
    void toggleFileView() {
        if (splitPane.getItems().get(0).equals(treeAnchor)) {
            splitPane.getItems().remove(0);
        } else {
            splitPane.getItems().add(0, treeAnchor);
            splitPane.getDividers().get(0).setPosition(0.2);
        }
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        fileTree.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                FileItem fileItem = (FileItem) fileTree.getSelectionModel().getSelectedItem();
                if (fileItem != null) {
                    File file = fileItem.getFile();
                    if (file.isFile() && file.exists()) {
                        int index = -1;
                        for (Tab t: tabPane.getTabs()) {
                            if (t.getUserData() == file) {
                                index = tabPane.getTabs().indexOf(t);
                            }
                        }
                        if (index != -1) {
                            tabPane.getSelectionModel().select(index);
                        } else {
                            if (tabPane.getSelectionModel().getSelectedItem().getUserData() != null) {
                                tabPane.createTab(file);
                            } else {
                                tabPane.setTab(file);
                            }
                        }
                    } else if (file.isDirectory() && file.exists()) {
                        if (fileItem.isExpanded()) {
                            fileItem.setExpanded(false);
                        } else {
                            fileItem.setExpanded(true);
                        }
                    }
                }
            }
        });

        RecentFileList.recentFiles.addListener((ListChangeListener<File>) change -> {
            if (!(RecentFileList.recentFiles.isEmpty()) && RecentFileList.recentFiles.get(0) != null) {
                try {
                    RecentFileList.saveRecent();
                } catch (IOException e) {
                    System.out.println(e);
                }
                openRecentDrop.getItems().clear();
                for (File file : RecentFileList.recentFiles) {
                    if (file.isFile() && file.exists()) {
                        MenuItem m = new MenuItem(file.getName());
                        m.setUserData(file);
                        m.setOnAction(e -> {
                            if (tabPane.getTabs().size() == 1 && tabPane.getCode().equals("")) {
                                tabPane.setTab(file);
                            } else {
                                tabPane.createTab(file);
                            }
                        });
                        openRecentDrop.getItems().add(m);
                    }
                }
            }
        });

        for (String mode: CodeEditor.modes) {
            MenuItem m = new MenuItem(mode);
            m.setOnAction(e -> {
                ((CodeTab) tabPane.getSelectionModel().getSelectedItem()).codeEditor.setMode(m.getText());
            });
            syntaxModeDrop.getItems().add(m);
        }

        fileTree.setIcon(icon);
        menuBar.useSystemMenuBarProperty().set(true);

        createTab();
        RecentFileList.loadRecent();
    }
}
