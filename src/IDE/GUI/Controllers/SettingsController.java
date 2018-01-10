package IDE.GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

public class SettingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private ObservableList<String> ignoredFileTypes = FXCollections.observableArrayList("");

    @FXML
    private TableView<String> ignoredFilesTable = new TableView<>();

    @FXML
    private TableColumn<String, String> ignoredRemoves;

    @FXML
    void initialize() {
        ignoredRemoves.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()));

        ignoredRemoves.setCellFactory(TextFieldTableCell.forTableColumn());
        ignoredRemoves.setOnEditCommit(t -> {
            int num = 0;
            for (String s: ignoredFilesTable.getItems()) {
                if (s != null || !s.equals("")) {
                    num++;
                }
            }
            if (num == ignoredFilesTable.getItems().size()) {
                //ignoredFileTypes.add("");
                ignoredFilesTable.getItems().add("");
            }
            System.out.println(ignoredFileTypes);
        });

        ignoredFilesTable.setItems(ignoredFileTypes);
    }
}
