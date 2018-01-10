package IDE.GUI.Components;

import IDE.Utils.Permissions;
import IDE.Utils.RecentFileList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class IDEPane extends TabPane {
    private void addTab(Tab tab) {
        getTabs().add(tab);
        getSelectionModel().select(tab);
    }

    public void createTab() {
        CodeTab codeTab = new CodeTab();
        codeTab.setOnCloseRequest(arg0 -> closeTab());
        addTab(codeTab);
    }

    private boolean isImage(File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return Arrays.asList(ImageIO.getWriterFormatNames()).contains(ext);
    }

    public void createTab(File file) {
        if (isImage(file)) {
            ImageTab imageTab = new ImageTab(file);
            imageTab.setOnCloseRequest(arg0 -> closeTab());
            addTab(imageTab);
            System.out.println("Created ImageTab: " + imageTab.getText());
        } else {
            CodeTab codeTab = new CodeTab(file);
            codeTab.setOnCloseRequest(arg0 -> closeTab());
            addTab(codeTab);
            System.out.println("Created CodeTab: " + codeTab.getText());
        }
        RecentFileList.addRecent(file);
    }

    public void setTab(File file) {
        getSelectionModel().getSelectedItem().setText(file.getName());
        getSelectionModel().getSelectedItem().setUserData(file);
        if (isImage(file)) {
            ImageTab imageTab = new ImageTab(file);
            getSelectionModel().getSelectedItem().setContent(imageTab.getContent());
            RecentFileList.addRecent(file);
        } else {
            CodeTab codeTab = new CodeTab(file);
            getSelectionModel().getSelectedItem().setContent(codeTab.getContent());
            RecentFileList.addRecent(file);
        }
    }

    public void closeTab() {
        getTabs().remove(getSelectionModel().getSelectedItem());
        if (getTabs().isEmpty()) {
            createTab();
        }
    }

    private Parent getCurrentContent() {
        return ((Parent) getSelectionModel().getSelectedItem().getContent());
    }

    public CodeEditor getCodeEditor() {
        if (getSelectionModel().getSelectedItem() instanceof CodeTab) {
            SplitPane splitPane = (SplitPane) getSelectionModel().getSelectedItem().getContent();
            return ((CodeEditor) splitPane.getItems().get(0));
        }
        return null;
    }

    public String getCode() {
        if (getSelectionModel().getSelectedItem() instanceof CodeTab) {
            SplitPane splitPane = (SplitPane) getSelectionModel().getSelectedItem().getContent();
            return ((CodeEditor) splitPane.getItems().get(0)).getText();
        }
        return null;
    }

    public BufferedImage getImage() {
        if (getSelectionModel().getSelectedItem() instanceof ImageTab) {
            return SwingFXUtils.fromFXImage(((ImageView) getCurrentContent().getChildrenUnmodifiable().get(0)).getImage(), null);
        }
        return null;
    }

    public boolean checkTabAccess(Permissions p) {
        return getSelectionModel().getSelectedItem() instanceof CodeTab && ((CustomTab) getSelectionModel().getSelectedItem()).Enabled(p);
    }
}
