package main.java.me.joshmcfarlin.AstroIDE.GUI.Components;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class FileItem extends TreeItem<String> {
    private File file;
    private Image icon;

    FileItem(String value, File file) {
        super(value);
        this.file = file;
        setup();
    }

    FileItem(String value, Image icon, File file) {
        super(value);
        this.file = file;
        this.icon = icon;
        ImageView img = new ImageView(icon);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setFitHeight(10);
        setGraphic(img);
        setup();
    }

    private void setup() throws NullPointerException {
        expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (getChildren().isEmpty() || getChildren().get(0).getValue().equals("")) {
                if (!getChildren().isEmpty() && getChildren().get(0).getValue().equals("")) {
                    getChildren().remove(0);
                }
                for (File childFile : file.listFiles()) {
                    if (childFile.exists()) {
                        FileItem childItem;
                        if (childFile.isDirectory()) {
                            childItem = new FileItem(childFile.getName(), icon, childFile);
                            childItem.getChildren().add(new TreeItem<>(""));
                        } else {
                            childItem = new FileItem(childFile.getName(), childFile);
                        }
                        getChildren().add(childItem);
                    }
                }
            }
        });
    }

    void setIcon(Image icon) {
        this.icon = icon;
        ImageView img = new ImageView(icon);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setFitHeight(10);
        setGraphic(img);
    }

    public File getFile() {
        return file;
    }
}
