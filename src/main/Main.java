package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../poker/views/poker-home-scene.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        primaryStage.setTitle("Poker");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
