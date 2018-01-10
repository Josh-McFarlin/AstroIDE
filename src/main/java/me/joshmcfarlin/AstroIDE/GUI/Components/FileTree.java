package main.java.me.joshmcfarlin.AstroIDE.GUI.Components;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import java.io.File;

public class FileTree extends TreeView<String> {
    private File rootFile;
    private Image icon;
    private FileItem workingDirItem;

    public FileTree() {
        rootFile = new File("").getAbsoluteFile();
        workingDirItem = new FileItem(this.rootFile.getName(), this.rootFile);
        workingDirItem.setExpanded(true);
        setRoot(workingDirItem);
        setContextMenu(getMenu());
    }

    public void setIcon(Image icon) {
        this.icon = icon;
        workingDirItem.setIcon(icon);
        for (TreeItem<String> childItem: workingDirItem.getChildren()) {
            if (!childItem.getChildren().isEmpty()) {
                ((FileItem) childItem).setIcon(icon);
            }
        }
    }

    private ContextMenu getMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem parentRootButton = new MenuItem("Set Root to Parent");
        parentRootButton.setOnAction(t -> {
            File file = ((FileItem) getSelectionModel().getSelectedItem()).getFile();
            setRoot(file.getParentFile());
        });
        contextMenu.getItems().add(parentRootButton);
        return contextMenu;
    }

    public void setRoot(File file) {
        rootFile = file;
        FileItem wdi = new FileItem(this.rootFile.getName(), icon, this.rootFile);
        wdi.setExpanded(true);
        setRoot(wdi);
    }
}
