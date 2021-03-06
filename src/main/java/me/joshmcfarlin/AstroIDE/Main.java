package main.java.me.joshmcfarlin.AstroIDE;

import main.java.me.joshmcfarlin.AstroIDE.GUI.Controllers.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main/java/me/joshmcfarlin/AstroIDE/GUI/Resources/FXML/IDE.fxml"));
        primaryStage.setTitle("Astro");
        Scene scene = new Scene(root, 1280, 900);
        scene.getStylesheets().add(AppController.class.getResource("/main/java/me/joshmcfarlin/AstroIDE/GUI/Resources/CSS/Default.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}